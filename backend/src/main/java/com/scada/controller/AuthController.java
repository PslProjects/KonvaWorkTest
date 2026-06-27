package com.scada.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scada.entities.LoginRequest;
import com.scada.entities.User;
import com.scada.exception.InvalidCredentialsException;
import com.scada.service.UserService;
import com.scada.sess.AdminSessionTracker;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"https://scada.pratikshat.com", "http://198.7.114.147:9999", "http://localhost:9999", "http://localhost:4200"})
public class AuthController {

    @Autowired
    private UserService service;

    // --------- SIGNUP ----------
    @PostMapping(value = "/add-user", consumes = "multipart/form-data")
    public ResponseEntity<?> addUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("designation") String designation,
            @RequestParam(value = "photo", required = false) MultipartFile photo
    ) {
        try {
            byte[] photoBytes = null;

            if (photo != null && !photo.isEmpty()) {
                photoBytes = compressImage(photo.getBytes()); // ✅ compress karo
            }

            service.addUser(username, password, designation, photoBytes);
            return ResponseEntity.ok(Map.of("message", "User added successfully"));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // ✅ Compression method
    private byte[] compressImage(byte[] originalBytes) throws Exception {
        BufferedImage originalImage = null;

        // ✅ Step 1: Safe read with colorspace fix
        try (ByteArrayInputStream bais = new ByteArrayInputStream(originalBytes)) {
            originalImage = ImageIO.read(bais);
        }

        // ✅ Step 2: Agar ImageIO fail kare to original bytes return karo (no crash)
        if (originalImage == null) {
            System.out.println("⚠️ ImageIO could not read image, saving original.");
            return originalBytes;
        }

        // ✅ Step 3: FORCE convert to RGB (CMYK/unusual colorspace fix)
        BufferedImage rgbImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );
        Graphics2D g2d = rgbImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, java.awt.Color.WHITE, null); // white background
        g2d.dispose();
        originalImage = rgbImage;

        // ✅ Step 4: Resize if too large
        int maxDimension = 800;
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        if (width > maxDimension || height > maxDimension) {
            double scale = Math.min((double) maxDimension / width, (double) maxDimension / height);
            int newWidth = (int) (width * scale);
            int newHeight = (int) (height * scale);

            BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = resized.createGraphics();
            g.drawImage(originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
            g.dispose();
            originalImage = resized;
        }

        // ✅ Step 5: JPEG compression at 70% quality
        byte[] compressed = toJpegBytes(originalImage, 0.7f);

        // ✅ Step 6: Still > 1MB? Compress harder at 50%
        if (compressed.length > 1024 * 1024) {
            compressed = toJpegBytes(originalImage, 0.5f);
        }

        System.out.println("Original: " + originalBytes.length / 1024 + " KB → Compressed: " + compressed.length / 1024 + " KB");
        return compressed;
    }

    // ✅ Helper method
    private byte[] toJpegBytes(BufferedImage image, float quality) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
        ImageWriteParam params = writer.getDefaultWriteParam();
        params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        params.setCompressionQuality(quality);
        ImageOutputStream ios = ImageIO.createImageOutputStream(out);
        writer.setOutput(ios);
        writer.write(null, new javax.imageio.IIOImage(image, null, null), params);
        writer.dispose();
        ios.close();
        return out.toByteArray();
    }
    @PostMapping("/add-admin")
    public ResponseEntity<?> addAdmin(@RequestBody LoginRequest req) {

        try {
            service.addAdmin(req.getUsername(), req.getPassword());
            return ResponseEntity.ok(Map.of("message", "Admin added successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // --------- LOGIN ----------
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest req) throws InvalidCredentialsException {

        User user = service.validateUser(req.getUsername(), req.getPassword());

        if (user != null) {
            return Map.of(
                    "token", "LOGGED_" + user.getId(),
                    "username", user.getUsername(),
                    "role", user.getRole()   // ★ MUST ADD THIS ★
            );
        }

        throw new InvalidCredentialsException("Invalid credentials");
    }
    @PostMapping("/admin/login")
    public Map<String, String> adminLogin(@RequestBody LoginRequest req) throws InvalidCredentialsException {

        User user = service.validateUser(req.getUsername(), req.getPassword());

        if (user != null && "ADMIN".equals(user.getRole())) {

            // ⭐ FIX: Set active admin globally (overwrite old admin)
            AdminSessionTracker.setCurrentAdmin(user.getUsername());

            return Map.of(
                    "token", "ADMIN_" + user.getId(),
                    "username", user.getUsername(),
                    "role", "ADMIN"
            );
        }

        throw new InvalidCredentialsException("Admin credentials required");
    }
    @PostMapping("/admin/authorize")
    public ResponseEntity<?> authorizeAdmin(@RequestBody Map<String, String> req) {

        String username = req.get("username");
        String password = req.get("password");
        String masterPin = req.get("masterPin");

        // ✅ Master PIN
        if (!"999999".equals(masterPin)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid Master PIN"));
        }

        // ✅ Validate admin credentials
        User admin = service.validateUser(username, password);

        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Admin authentication failed"));
        }

        return ResponseEntity.ok(Map.of("message", "Authorized"));
    }
    @GetMapping("/admin/users")
    public ResponseEntity<?> getAllUsers() {

        List<User> users = service.getAllUsers(); // we'll add this method

        List<String> usernames = users.stream()
                .filter(u -> "USER".equals(u.getRole()))
                .map(User::getUsername)
                .collect(Collectors.toList());

        return ResponseEntity.ok(usernames);
    }




}