package com.scada.service;

import com.scada.entities.FaultValidationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class FaultService {

    @Autowired
    private FaultConfigService configService;

    // ----------------------------------------------------
    // MAIN VALIDATION ENTRY
    // ----------------------------------------------------
   public FaultValidationResponse validateFaultResolution(String faultLine, List<String> seq) {

    System.err.println("User Sequence: " + seq);

    List<String> correctSeq = buildCorrectSequence(faultLine, seq);

    // ---------------- LG7 ----------------
    if (faultLine.equals("LG7")) {
        String series = detectSeriesLG7(seq);
        if (series == null)
            return new FaultValidationResponse(false, "❌ Could not detect LG7 series", correctSeq, seq);

        Map<String, List<String>> blocks = configService.getFlexConfig("LG7", series);
        if (blocks == null)
            return new FaultValidationResponse(false, "❌ No LG7 config found", correctSeq, seq);

        boolean ok = validateLG7(seq, blocks, series);
        String msg = ok ? "✅ Fault resolved properly" : "⚠️ Fault resolved but not in proper way";
        return new FaultValidationResponse(ok, msg, correctSeq, seq);
    }

    // ---------------- LG8 ----------------
    if (faultLine.equals("LG8")) {
        String series = detectSeriesLG8(seq);
        if (series == null)
            return new FaultValidationResponse(false, "❌ Could not detect LG8 series", correctSeq, seq);

        Map<String, List<String>> blocks = configService.getFlexConfig("LG8", series);
        if (blocks == null)
            return new FaultValidationResponse(false, "❌ No LG8 config found", correctSeq, seq);

        boolean ok = validateLG8(seq, blocks, series);
        String msg = ok ? "✅ Fault resolved properly" : "⚠️ Fault resolved but not in proper way";
        return new FaultValidationResponse(ok, msg, correctSeq, seq);
    }

    // ---------------- LG11 ----------------
    if (faultLine.equals("LG11")) {
        String series = detectSeriesLG11(seq);
        if (series == null)
            return new FaultValidationResponse(false, "❌ Could not detect LG11 series", correctSeq, seq);

        Map<String, List<String>> blocks = configService.getFlexConfig("LG11", series);
        if (blocks == null)
            return new FaultValidationResponse(false, "❌ No LG11 config found", correctSeq, seq);

        boolean ok = validateLG11(seq, blocks, series);
        String msg = ok ? "✅ Fault resolved properly" : "⚠️ Fault resolved but not in proper way";
        return new FaultValidationResponse(ok, msg, correctSeq, seq);
    }

    // ---------------- LG12 ----------------
    if (faultLine.equals("LG12")) {
        String series = detectSeriesLG12(seq);
        if (series == null)
            return new FaultValidationResponse(false, "❌ Could not detect LG12 series", correctSeq, seq);

        boolean ok = validateLG12(seq, series);
        String msg = ok ? "✅ Fault resolved properly" : "⚠️ Fault resolved but not in proper way";
        return new FaultValidationResponse(ok, msg, correctSeq, seq);
    }

    // ---------------- LG24 ----------------
    if (faultLine.equals("LG24")) {
        String variant = detectLG24Variant(seq);
        if (variant == null)
            return new FaultValidationResponse(false, "❌ Could not detect RS15/RS17 variant for LG24", correctSeq, seq);

        boolean ok = variant.equals("RS17")
                ? validateLG24_RS17(seq)
                : validateLG24_RS15(seq);

        String msg = ok ? "✅ Fault resolved properly" : "⚠️ Fault resolved but not in proper way";
        return new FaultValidationResponse(ok, msg, correctSeq, seq);
    }

    // ---------------- LG25 ----------------
    if (faultLine.equals("LG25")) {
        String variant = detectLG25Variant(seq);
        if (variant == null)
            return new FaultValidationResponse(false, "❌ Could not detect RS15/RS17 variant for LG25", correctSeq, seq);

        boolean ok = variant.equals("RS15")
                ? validateLG25_RS15(seq)
                : validateLG25_RS17(seq);

        String msg = ok ? "✅ Fault resolved properly" : "⚠️ Fault resolved but not in proper way";
        return new FaultValidationResponse(ok, msg, correctSeq, seq);
    }

    // ---------------- LG38 ----------------
    if (faultLine.equals("LG38")) {
        String variant = detectLG38Variant(seq);
        if (variant == null)
            return new FaultValidationResponse(false, "❌ Could not detect RS15/RS17 variant for LG38", correctSeq, seq);

        boolean ok = variant.equals("RS15")
                ? validateLG38_RS15(seq)
                : validateLG38_RS17(seq);

        String msg = ok ? "✅ Fault resolved properly" : "⚠️ Fault resolved but not in proper way";
        return new FaultValidationResponse(ok, msg, correctSeq, seq);
    }

    // ---------------- LG39 ----------------
    if (faultLine.equals("LG39")) {
        String variant = detectLG39Variant(seq);
        if (variant == null)
            return new FaultValidationResponse(false, "❌ Could not detect RS15/RS17 variant for LG39", correctSeq, seq);

        boolean ok = variant.equals("RS15")
                ? validateLG39_RS15(seq)
                : validateLG39_RS17(seq);

        String msg = ok ? "✅ Fault resolved properly" : "⚠️ Fault resolved but not in proper way";
        return new FaultValidationResponse(ok, msg, correctSeq, seq);
    }

    // ---------------- LG45 ----------------
    if (faultLine.equals("LG45")) {
        String variant = detectLG45Variant(seq);
        if (variant == null)
            return new FaultValidationResponse(false, "❌ Could not detect RS15/RS17 variant for LG45", correctSeq, seq);

        boolean ok = variant.equals("RS15")
                ? validateLG45_RS15(seq)
                : validateLG45_RS17(seq);

        String msg = ok ? "✅ Fault resolved properly" : "⚠️ Fault resolved but not in proper way";
        return new FaultValidationResponse(ok, msg, correctSeq, seq);
    }

    // ---------------- LG46 ----------------
    if (faultLine.equals("LG46")) {
        String variant = detectLG46Variant(seq);
        if (variant == null)
            return new FaultValidationResponse(false, "❌ Could not detect RS15/RS17 variant for LG46", correctSeq, seq);

        boolean ok = variant.equals("RS15")
                ? validateLG46_RS15(seq)
                : validateLG46_RS17(seq);

        String msg = ok ? "✅ Fault resolved properly" : "⚠️ Fault resolved but not in proper way";
        return new FaultValidationResponse(ok, msg, correctSeq, seq);
    }

    return new FaultValidationResponse(false, "❌ Validation not implemented for " + faultLine, correctSeq, seq);
}

 // ===============================
//  HELPERS FOR CORRECT SEQUENCE
// ===============================

/**
 * Utility: concatenate START + MIDDLE + END blocks
 */
private List<String> concatBlocks(List<String> start, List<String> middle, List<String> end) {
    List<String> result = new ArrayList<>();
    if (start != null && !start.isEmpty()) result.addAll(start);
    if (middle != null && !middle.isEmpty()) result.addAll(middle);
    if (end != null && !end.isEmpty()) result.addAll(end);
    return result;
}

/**
 * Utility: read canonical sequence directly from config (LG7, LG8, LG11, LG12).
 */
private List<String> buildFromConfig(String line, String variantOrSeries) {
    Map<String, List<String>> blocks = configService.getFlexConfig(line, variantOrSeries);
    if (blocks == null) return Collections.emptyList();
    return concatBlocks(blocks.get("START"), blocks.get("MIDDLE"), blocks.get("END"));
}

/**
 * Main correct-sequence builder
 * Always returns a minimal valid (O1) canonical sequence for that line.
 */
private List<String> buildCorrectSequence(String faultLine, List<String> userSeq) {
    switch (faultLine) {

        // ---------- LG7 ----------
        case "LG7": {
            String series = detectSeriesLG7(userSeq);
            if (series == null) series = "SERIES1";   // fallback default
            return buildFromConfig("LG7", series);
        }

        // ---------- LG8 ----------
        case "LG8": {
            String series = detectSeriesLG8(userSeq);
            if (series == null) series = "SERIES1";
            return buildFromConfig("LG8", series);
        }

        // ---------- LG11 ----------
        case "LG11": {
            String series = detectSeriesLG11(userSeq);
            if (series == null) series = "SERIES1";
            return buildFromConfig("LG11", series);
        }

        // ---------- LG12 ----------
        case "LG12": {
            String series = detectSeriesLG12(userSeq);
            if (series == null) series = "SERIES1";
            return buildFromConfig("LG12", series);
        }

        // ---------- LG24 ----------
        case "LG24": {
            String variant = detectLG24Variant(userSeq);
            if (variant == null) variant = "RS17";  // fallback
            return buildCanonicalLG24(variant);
        }

        // ---------- LG25 ----------
        case "LG25": {
            String variant = detectLG25Variant(userSeq);
            if (variant == null) variant = "RS15";  // RS15 variant more “rich”
            return buildCanonicalLG25(variant);
        }

        // ---------- LG38 ----------
        case "LG38": {
            String variant = detectLG38Variant(userSeq);
            if (variant == null) variant = "RS15";
            return buildCanonicalLG38(variant);
        }

        // ---------- LG39 ----------
        case "LG39": {
            String variant = detectLG39Variant(userSeq);
            if (variant == null) variant = "RS15";
            return buildCanonicalLG39(variant);
        }

        // ---------- LG45 ----------
        case "LG45": {
            String variant = detectLG45Variant(userSeq);
            if (variant == null) variant = "RS15";
            return buildCanonicalLG45(variant);
        }

        // ---------- LG46 ----------
        case "LG46": {
            String variant = detectLG46Variant(userSeq);
            if (variant == null) variant = "RS15";
            return buildCanonicalLG46(variant);
        }

        default:
            return Collections.emptyList();
    }
}
//===============================
//CANONICAL SEQUENCES (O1 style)
//===============================

//---- LG24: minimal valid sequence (no extra tail) ----
private List<String> buildCanonicalLG24(String variant) {
List<String> s = new ArrayList<>();

if ("RS17".equals(variant)) {
    // START: RS18 + {RS17,RS26,RS38,RS45} any order → fixed canonical order
    s.addAll(Arrays.asList("RS18", "RS17", "RS26", "RS38", "RS45"));

    // MIDDLE: RS18, {RS15,RS17} any order → canonical
    s.addAll(Arrays.asList("RS18", "RS15", "RS17"));
} else { // RS15 variant
    s.addAll(Arrays.asList("RS18", "RS15", "RS26", "RS38", "RS45"));
    // no middle
}

// END core: RS18, RS37, X, RS25, RS26  (choose X=RS38)
s.addAll(Arrays.asList("RS18", "RS37", "RS38", "RS25", "RS26"));

// O1: no extra tail (tail is optional and can be empty)
return s;
}

//---- LG25: minimal valid sequence ----
private List<String> buildCanonicalLG25(String variant) {
List<String> s = new ArrayList<>();

if ("RS15".equals(variant)) {
    // START
    s.addAll(Arrays.asList("RS18", "RS15", "RS26", "RS38", "RS45"));

    // MIDDLE: RS18 → {RS15,RS17} → RS18
    s.addAll(Arrays.asList("RS18", "RS15", "RS17", "RS18"));

    // END core: RS39, X, RS27, RS26 (X = RS38)
    s.addAll(Arrays.asList("RS39", "RS38", "RS27", "RS26"));

    // tail optional → O1: none
} else { // RS17 variant
    // START
    s.addAll(Arrays.asList("RS18", "RS17", "RS26", "RS38", "RS45"));

    // optional single RS18 before END → O1: keep minimal, so skip

    // END core
    s.addAll(Arrays.asList("RS39", "RS38", "RS27", "RS26"));
}

return s;
}

//---- LG38: minimal valid sequence ----
private List<String> buildCanonicalLG38(String variant) {
List<String> s = new ArrayList<>();

if ("RS15".equals(variant)) {
    // START
    s.addAll(Arrays.asList("RS18", "RS15", "RS26", "RS38", "RS45"));

    // MIDDLE RS15: RS18, RS37, X, RS25, RS26   (X=RS38)
    s.addAll(Arrays.asList("RS18", "RS37", "RS38", "RS25", "RS26"));

    // END core: RS18, RS26   (tail optional)
    s.addAll(Arrays.asList("RS18", "RS26"));

} else { // RS17
    // START
    s.addAll(Arrays.asList("RS18", "RS17", "RS26", "RS38", "RS45"));

    // MIDDLE RS17: RS18, {RS17,RS15}, RS18, RS37, X, RS25, RS26
    s.addAll(Arrays.asList("RS18", "RS17", "RS15", "RS18", "RS37", "RS38", "RS25", "RS26"));

    // END core
    s.addAll(Arrays.asList("RS18", "RS26"));
}

return s;
}

//---- LG39: minimal valid sequence (with tail satisfying rules) ----
private List<String> buildCanonicalLG39(String variant) {
List<String> s = new ArrayList<>();

if ("RS15".equals(variant)) {
    // START
    s.addAll(Arrays.asList("RS18", "RS15", "RS26", "RS38", "RS45"));

    // MIDDLE RS15: RS18, {RS17,RS15}, RS18, RS39, X, RS27, RS26
    // choose X = RS38
    s.addAll(Arrays.asList("RS18", "RS15", "RS17", "RS18", "RS39", "RS38", "RS27", "RS26"));

    // END: RS18, RS26, then minimal tail with RS17 + RS18
    s.addAll(Arrays.asList("RS18", "RS26", "RS17", "RS18"));

} else { // RS17
    // START
    s.addAll(Arrays.asList("RS18", "RS17", "RS26", "RS38", "RS45"));

    // MIDDLE RS17: RS18, RS39, X, RS27, RS26  (X=RS38)
    s.addAll(Arrays.asList("RS18", "RS39", "RS38", "RS27", "RS26"));

    // END: RS18, RS26 + tail RS17,RS18
    s.addAll(Arrays.asList("RS18", "RS26", "RS17", "RS18"));
}

return s;
}

//---- LG45: minimal valid END with RS15, RS18, X ----
private List<String> buildCanonicalLG45(String variant) {
List<String> s = new ArrayList<>();

if ("RS15".equals(variant)) {
    // START
    s.addAll(Arrays.asList("RS18", "RS15", "RS26", "RS38", "RS45"));

    // MIDDLE RS15: RS18, RS37, X   (X=RS38)
    s.addAll(Arrays.asList("RS18", "RS37", "RS38"));

    // END: first RS18, then tail containing RS15, RS18, one X
    s.addAll(Arrays.asList("RS18", "RS15", "RS18", "RS38"));

} else { // RS17
    // START
    s.addAll(Arrays.asList("RS18", "RS17", "RS26", "RS38", "RS45"));

    // MIDDLE RS17: RS18, {RS17,RS15}, RS18, RS37, X
    s.addAll(Arrays.asList("RS18", "RS17", "RS15", "RS18", "RS37", "RS38"));

    // END (same pattern): RS18 + tail RS15,RS18,X
    s.addAll(Arrays.asList("RS18", "RS15", "RS18", "RS38"));
}

return s;
}

//---- LG46: minimal valid END with RS17 & RS18 ----
private List<String> buildCanonicalLG46(String variant) {
List<String> s = new ArrayList<>();

if ("RS15".equals(variant)) {
    // START
    s.addAll(Arrays.asList("RS18", "RS15", "RS26", "RS38", "RS45"));

    // MIDDLE RS15: RS18, {RS17,RS15}, RS18, RS39, X   (X=RS38)
    s.addAll(Arrays.asList("RS18", "RS17", "RS15", "RS18", "RS39", "RS38"));

    // END: RS18, X, then tail with RS17, RS18
    s.addAll(Arrays.asList("RS18", "RS38", "RS17", "RS18"));

} else { // RS17
    // START
    s.addAll(Arrays.asList("RS18", "RS17", "RS26", "RS38", "RS45"));

    // MIDDLE RS17: RS18, RS39, X (X=RS38)
    s.addAll(Arrays.asList("RS18", "RS39", "RS38"));

    // END: same: RS18, X, RS17, RS18
    s.addAll(Arrays.asList("RS18", "RS38", "RS17", "RS18"));
}

return s;
}


    // ====================================================
    // COMMON START VALIDATOR (LG24, 25, 38, 39, 45, 46)
    // ====================================================
    private boolean matchCommonStart(List<String> seq, int index, String variant) {
        if (index + 5 > seq.size()) return false;

        if (!"RS18".equals(seq.get(index))) return false;

        // next 4 must be {variant, RS26, RS38, RS45} in ANY order
        Set<String> expected = Set.of(variant, "RS26", "RS38", "RS45");
        Set<String> found = new HashSet<>(seq.subList(index + 1, index + 5));

        return expected.equals(found);
    }

    // ====================================================
    // LG24 HELPERS
    // ====================================================

    private String detectLG24Variant(List<String> seq) {
        if (seq.contains("RS17")) return "RS17";
        if (seq.contains("RS15")) return "RS15";
        return null;
    }
    

    private boolean validateLG24_RS17(List<String> seq) {
        int index = 0;

        if (!matchCommonStart(seq, index, "RS17")) return false;
        index += 5;

        if (!matchLG24Middle_RS17(seq, index)) return false;
        index += 3;

        return matchLG24End(seq, index);
    }

    private boolean validateLG24_RS15(List<String> seq) {
        int index = 0;

        if (!matchCommonStart(seq, index, "RS15")) return false;
        index += 5;

        return matchLG24End(seq, index);
    }

    private boolean matchLG24Middle_RS17(List<String> seq, int index) {
        if (index + 3 > seq.size()) return false;
        if (!seq.get(index).equals("RS18")) return false;

        Set<String> expected = Set.of("RS15", "RS17");
        Set<String> actual = Set.of(seq.get(index + 1), seq.get(index + 2));

        return expected.equals(actual);
    }

    private boolean matchLG24End(List<String> seq, int index) {
        if (index + 5 > seq.size()) return false;

        if (!seq.get(index).equals("RS18")) return false;
        if (!seq.get(index + 1).equals("RS37")) return false;

        String x = seq.get(index + 2);
        if (!(x.equals("RS38") || x.equals("RS45"))) return false;

        if (!seq.get(index + 3).equals("RS25")) return false;
        if (!seq.get(index + 4).equals("RS26")) return false;

        // Tail: only RS37 / RS38 / RS45 allowed
        for (int i = index + 5; i < seq.size(); i++) {
            String s = seq.get(i);
            if (!(s.equals("RS37") || s.equals("RS38") || s.equals("RS45"))) return false;
        }
        return true;
    }

    // ====================================================
    // LG25 HELPERS
    // ====================================================
    private String detectLG25Variant(List<String> seq) {
        if (seq.contains("RS15")) return "RS15"; // RS15 variant has both 15 & 17
        if (seq.contains("RS17")) return "RS17";
        return null;
    }

    private boolean validateLG25_RS15(List<String> seq) {
        int index = 0;

        if (!matchCommonStart(seq, index, "RS15")) return false;
        index += 5;

        if (!matchLG25Middle_RS15(seq, index)) return false;
        index += 4;

        return matchLG25End(seq, index);
    }

    private boolean validateLG25_RS17(List<String> seq) {
        int index = 0;

        if (!matchCommonStart(seq, index, "RS17")) return false;
        index += 5;

        // Optional single RS18 before END
        if (index < seq.size() && "RS18".equals(seq.get(index))) {
            index++;
        }

        return matchLG25End(seq, index);
    }

    // MIDDLE for RS15: RS18 → {RS15, RS17} (any order) → RS18
    private boolean matchLG25Middle_RS15(List<String> seq, int index) {
        if (index + 4 > seq.size()) return false;

        String m0 = seq.get(index);
        String m1 = seq.get(index + 1);
        String m2 = seq.get(index + 2);
        String m3 = seq.get(index + 3);

        if (!"RS18".equals(m0)) return false;
        if (!"RS18".equals(m3)) return false;

        Set<String> midSet = Set.of(m1, m2);
        return midSet.equals(Set.of("RS15", "RS17"));
    }

    // END: RS39 → X (RS38 or RS45) → RS27 → RS26 → optional tail:
    //  - optional RS39 (only once)
    //  - optional Y (the other of RS38/RS45), only once
    private boolean matchLG25End(List<String> seq, int index) {
        int rem = seq.size() - index;
        if (rem < 4) return false;

        String s0 = seq.get(index);
        String s1 = seq.get(index + 1);
        String s2 = seq.get(index + 2);
        String s3 = seq.get(index + 3);

        if (!"RS39".equals(s0)) return false;
        if (!( "RS38".equals(s1) || "RS45".equals(s1) )) return false;
        if (!"RS27".equals(s2)) return false;
        if (!"RS26".equals(s3)) return false;

        String x = s1;
        String y = x.equals("RS38") ? "RS45" : "RS38";

        boolean usedOpt39 = false;
        boolean usedOptY = false;

        for (int i = index + 4; i < seq.size(); i++) {
            String v = seq.get(i);

            if ("RS39".equals(v)) {
                if (usedOpt39) return false;
                usedOpt39 = true;
            } else if (y.equals(v)) {
                if (usedOptY) return false;
                usedOptY = true;
            } else {
                return false;
            }
        }

        return true;
    }

    // ====================================================
    // LG38 HELPERS
    // ====================================================
    private String detectLG38Variant(List<String> seq) {
        if (seq.contains("RS17") && seq.contains("RS15")) return "RS17";
        if (seq.contains("RS15")) return "RS15";
        return null;
    }

    private boolean validateLG38_RS15(List<String> seq) {
        int index = 0;

        if (!matchCommonStart(seq, index, "RS15")) return false;
        index += 5;

        if (!matchLG38Middle_RS15(seq, index)) return false;
        index += 5;

        return matchLG38End(seq, index);
    }

    private boolean validateLG38_RS17(List<String> seq) {
        int index = 0;

        if (!matchCommonStart(seq, index, "RS17")) return false;
        index += 5;

        if (!matchLG38Middle_RS17(seq, index)) return false;
        index += 8;

        return matchLG38End(seq, index);
    }

    // MIDDLE RS15: RS18, RS37, (RS38/RS45), RS25, RS26
    private boolean matchLG38Middle_RS15(List<String> seq, int index) {
        if (index + 5 > seq.size()) return false;

        String a = seq.get(index);
        String b = seq.get(index + 1);
        String c = seq.get(index + 2);
        String d = seq.get(index + 3);
        String e = seq.get(index + 4);

        if (!a.equals("RS18")) return false;
        if (!b.equals("RS37")) return false;
        if (!(c.equals("RS38") || c.equals("RS45"))) return false;
        if (!d.equals("RS25")) return false;
        if (!e.equals("RS26")) return false;

        return true;
    }

    // MIDDLE RS17: RS18, {RS17,RS15} any order, RS18, RS37, (RS38/RS45), RS25, RS26
    private boolean matchLG38Middle_RS17(List<String> seq, int index) {
        if (index + 8 > seq.size()) return false;

        if (!seq.get(index).equals("RS18")) return false;

        Set<String> expectedTwo = Set.of("RS17", "RS15");
        Set<String> foundTwo = Set.of(seq.get(index + 1), seq.get(index + 2));
        if (!expectedTwo.equals(foundTwo)) return false;

        if (!seq.get(index + 3).equals("RS18")) return false;
        if (!seq.get(index + 4).equals("RS37")) return false;

        String x = seq.get(index + 5);
        if (!(x.equals("RS38") || x.equals("RS45"))) return false;

        if (!seq.get(index + 6).equals("RS25")) return false;
        if (!seq.get(index + 7).equals("RS26")) return false;

        return true;
    }

    // END LG38: RS18, RS26, tail ∈ { RS15, RS18, (one of RS45/RS38 at most once) }
    private boolean matchLG38End(List<String> seq, int index) {
        if (index + 2 > seq.size()) return false;

        if (!seq.get(index).equals("RS18")) return false;
        if (!seq.get(index + 1).equals("RS26")) return false;

        Set<String> allowed = Set.of("RS15", "RS18", "RS45", "RS38");
        boolean used45or38 = false;

        for (int i = index + 2; i < seq.size(); i++) {
            String s = seq.get(i);

            if (!allowed.contains(s)) return false;

            if (s.equals("RS45") || s.equals("RS38")) {
                if (used45or38) return false;
                used45or38 = true;
            }
        }

        return true;
    }

    // ====================================================
    // LG39 HELPERS
    // ====================================================

    // RS15 variant has both RS15 & RS17 in middle, so prefer RS15 if present
    private String detectLG39Variant(List<String> seq) {
        if (seq.contains("RS15")) return "RS15";
        if (seq.contains("RS17")) return "RS17";
        return null;
    }

    // LG39 RS15:
    // START: common
    // MIDDLE: RS18, {RS17, RS15} any order, RS18, RS39, (RS38/RS45), RS27, RS26
    // END: RS18, RS26, then [RS17, RS18] OR [RS17, RS45, RS18]
   private boolean validateLG39_RS15(List<String> seq) {
    int index = 0;

    // START
    if (!matchCommonStart(seq, index, "RS15")) return false;
    index += 5;

    // MIDDLE (RS15)
    // Extract the RS38/RS45 used in middle (5th index inside middle)
    String middleX = seq.get(index + 5);  // RS38 or RS45

    if (!matchLG39Middle_RS15(seq, index)) return false;
    index += 8; // RS15 middle size

    // END → must use OPPOSITE of middleX
    return matchLG39End(seq, index, middleX);
}


    // LG39 RS17:
    // START: common
    // MIDDLE: RS18, RS39, (RS38/RS45), RS27, RS26
    // END: RS18, RS26, then [RS17, RS18] OR [RS17, RS45, RS18]
   private boolean validateLG39_RS17(List<String> seq) {
	    int index = 0;

	    // START
	    if (!matchCommonStart(seq, index, "RS17")) return false;
	    index += 5;

	    // MIDDLE
	    // Here middleX is at index+2 in RS17 middle
	    String middleX = seq.get(index + 2);  // RS38 or RS45

	    if (!matchLG39Middle_RS17(seq, index)) return false;
	    index += 5; // RS17 middle size

	    // END with opposite of middleX
	    return matchLG39End(seq, index, middleX);
	}



    // RS18, {RS17,RS15} any order, RS18, RS39, (RS38/RS45), RS27, RS26
    private boolean matchLG39Middle_RS15(List<String> seq, int index) {
        if (index + 8 > seq.size()) return false;

        String m0 = seq.get(index);
        String m1 = seq.get(index + 1);
        String m2 = seq.get(index + 2);
        String m3 = seq.get(index + 3);
        String m4 = seq.get(index + 4);
        String m5 = seq.get(index + 5);
        String m6 = seq.get(index + 6);
        String m7 = seq.get(index + 7);

        if (!"RS18".equals(m0)) return false;

        Set<String> pair = Set.of(m1, m2);
        if (!pair.equals(Set.of("RS15", "RS17"))) return false;

        if (!"RS18".equals(m3)) return false;
        if (!"RS39".equals(m4)) return false;
        if (!(m5.equals("RS38") || m5.equals("RS45"))) return false;
        if (!"RS27".equals(m6)) return false;
        if (!"RS26".equals(m7)) return false;

        return true;
    }

    // RS18, RS39, (RS38/RS45), RS27, RS26
    private boolean matchLG39Middle_RS17(List<String> seq, int index) {
        if (index + 5 > seq.size()) return false;

        String a = seq.get(index);
        String b = seq.get(index + 1);
        String c = seq.get(index + 2);
        String d = seq.get(index + 3);
        String e = seq.get(index + 4);

        if (!"RS18".equals(a)) return false;
        if (!"RS39".equals(b)) return false;
        if (!(c.equals("RS38") || c.equals("RS45"))) return false;
        if (!"RS27".equals(d)) return false;
        if (!"RS26".equals(e)) return false;

        return true;
    }

  
private boolean matchLG39End(List<String> seq, int index, String middleX) {

    int rem = seq.size() - index;

    // Must start with RS18, RS26
    if (rem < 2) return false;
    if (!seq.get(index).equals("RS18")) return false;
    if (!seq.get(index + 1).equals("RS26")) return false;

    List<String> tail = seq.subList(index + 2, seq.size());

    if (tail.size() < 2 || tail.size() > 3) return false;

    // Must contain RS17 and RS18
    if (!tail.contains("RS17")) return false;
    if (!tail.contains("RS18")) return false;

    // Optional opposite of middleX
    String opposite = middleX.equals("RS38") ? "RS45" : "RS38";

    if (tail.size() == 2) {
        // Only RS17 + RS18 allowed
        return tail.contains("RS17") && tail.contains("RS18");
    }

    if (tail.size() == 3) {
        // Must contain RS17 + RS18 + oppositeX
        return tail.contains("RS17") && tail.contains("RS18") && tail.contains(opposite);
    }

    return false;
}



    
 // ====================================================
 // LG45 HELPERS
 // ====================================================
 private String detectLG45Variant(List<String> seq) {
     // RS17 variant uses RS17 (and may also have RS15 in middle)
     if (seq.contains("RS17")) return "RS17";
     // RS15-only variant (no RS17)
     if (seq.contains("RS15")) return "RS15";
     return null;
 }
//LG45 RS17:
//START: common
//MIDDLE: RS18, {RS17, RS15} any order, RS18, RS37, (RS38/RS45)
//END:   RS18, then FLEX tail (see matchLG45End)
private boolean validateLG45_RS17(List<String> seq) {
  int index = 0;

  if (!matchCommonStart(seq, index, "RS17")) return false;
  index += 5;

  if (!matchLG45Middle_RS17(seq, index)) return false;
  index += 6; // 6 elements consumed in middle

  return matchLG45End(seq, index);
}

//LG45 RS15:
//START: common
//MIDDLE: RS18, RS37, (RS38/RS45)
//END:   RS18, then FLEX tail (see matchLG45End)
private boolean validateLG45_RS15(List<String> seq) {
  int index = 0;

  if (!matchCommonStart(seq, index, "RS15")) return false;
  index += 5;

  if (!matchLG45Middle_RS15(seq, index)) return false;
  index += 3; // 3 elements in middle

  return matchLG45End(seq, index);
}
//RS18, {RS17, RS15} any order, RS18, RS37, (RS38/RS45)
private boolean matchLG45Middle_RS17(List<String> seq, int index) {
 if (index + 6 > seq.size()) return false;

 String m0 = seq.get(index);
 String m1 = seq.get(index + 1);
 String m2 = seq.get(index + 2);
 String m3 = seq.get(index + 3);
 String m4 = seq.get(index + 4);
 String m5 = seq.get(index + 5);

 if (!"RS18".equals(m0)) return false;

 // {RS17, RS15} any order
 if (!Set.of(m1, m2).equals(Set.of("RS17", "RS15"))) return false;

 if (!"RS18".equals(m3)) return false;
 if (!"RS37".equals(m4)) return false;
 if (!(m5.equals("RS38") || m5.equals("RS45"))) return false;

 return true;
}

//RS18, RS37, (RS38/RS45)
private boolean matchLG45Middle_RS15(List<String> seq, int index) {
 if (index + 3 > seq.size()) return false;

 String a = seq.get(index);
 String b = seq.get(index + 1);
 String c = seq.get(index + 2);

 if (!"RS18".equals(a)) return false;
 if (!"RS37".equals(b)) return false;
 if (!(c.equals("RS38") || c.equals("RS45"))) return false;

 return true;
}
//END for LG45 (both variants):
//RS18,
//then FLEX tail with:
//- RS15   → at least once
//- RS18   → at least once (besides the starting RS18)
//- RS38/RS45 → exactly ONE in total
//- RS26   → optional, any count
//- nothing else allowed
private boolean matchLG45End(List<String> seq, int index) {
 if (index >= seq.size()) return false;

 // First must be RS18
 if (!"RS18".equals(seq.get(index))) return false;
 index++; // tail starts here

 if (index >= seq.size()) return false; // need at least something in tail

 boolean seen15 = false;
 boolean seenExtra18 = false;
 boolean seenX = false; // RS38/RS45
 Set<String> allowed = Set.of("RS15", "RS18", "RS26", "RS38", "RS45");

 for (int i = index; i < seq.size(); i++) {
     String s = seq.get(i);

     if (!allowed.contains(s)) return false;

     switch (s) {
         case "RS15":
             seen15 = true;
             break;
         case "RS18":
             seenExtra18 = true;
             break;
         case "RS38":
         case "RS45":
             if (seenX) return false; // only one of RS38/RS45 allowed
             seenX = true;
             break;
         case "RS26":
             // fully allowed, no constraints on count
             break;
     }
 }

 // Must have at least one RS15, one extra RS18 and one of RS38/RS45
 return seen15 && seenExtra18 && seenX;
}
// ====================================================
 // LG46 HELPERS
 // ====================================================
private String detectLG46Variant(List<String> seq) {
    if (seq.contains("RS15")) return "RS15";
    if (seq.contains("RS17")) return "RS17";
    return null;
}
private boolean validateLG46_RS15(List<String> seq) {
    int index = 0;

    // START
    if (!matchCommonStart(seq, index, "RS15")) return false;
    index += 5;

    // MIDDLE (RS15)
    if (!matchLG46Middle_RS15(seq, index)) return false;
    index += 6; // size of RS15 middle

    // END
    return matchLG46End(seq, index);
}
private boolean matchLG46Middle_RS15(List<String> seq, int index) {
    if (index + 6 > seq.size()) return false;

    if (!seq.get(index).equals("RS18")) return false;

    Set<String> pair = Set.of(seq.get(index + 1), seq.get(index + 2));
    if (!pair.equals(Set.of("RS17", "RS15"))) return false;

    if (!seq.get(index + 3).equals("RS18")) return false;
    if (!seq.get(index + 4).equals("RS39")) return false;

    String x = seq.get(index + 5);
    return x.equals("RS38") || x.equals("RS45");
}
private boolean validateLG46_RS17(List<String> seq) {
    int index = 0;

    // START
    if (!matchCommonStart(seq, index, "RS17")) return false;
    index += 5;

    // MIDDLE (RS17)
    if (!matchLG46Middle_RS17(seq, index)) return false;
    index += 3; // size RS17 middle

    // END
    return matchLG46End(seq, index);
}
private boolean matchLG46Middle_RS17(List<String> seq, int index) {
    if (index + 3 > seq.size()) return false;

    if (!seq.get(index).equals("RS18")) return false;
    if (!seq.get(index + 1).equals("RS39")) return false;

    String x = seq.get(index + 2);
    return x.equals("RS38") || x.equals("RS45");
}
private boolean matchLG46End(List<String> seq, int index) {
    if (index + 2 > seq.size()) return false;

    if (!seq.get(index).equals("RS18")) return false;

    String x = seq.get(index + 1);
    if (!(x.equals("RS38") || x.equals("RS45"))) return false;

    boolean usedRS26 = false;
    boolean usedRS17 = false;
    boolean usedRS18 = false;

    for (int i = index + 2; i < seq.size(); i++) {
        String v = seq.get(i);

        if (v.equals("RS26")) {
            if (usedRS26) return false;
            usedRS26 = true;
        } else if (v.equals("RS17")) {
            if (usedRS17) return false;
            usedRS17 = true;
        } else if (v.equals("RS18")) {
            if (usedRS18) return false;
            usedRS18 = true;
        } else {
            return false;
        }
    }

    // RS17 and RS18 required at least once
    return usedRS17 && usedRS18;
}


    // ====================================================
    // COMMON FLEX + EXACT HELPERS
    // ====================================================
    private boolean matchFlexible(List<String> seq, int index, List<String> block) {
        if (index + block.size() > seq.size()) return false;
        return new HashSet<>(seq.subList(index, index + block.size()))
                .equals(new HashSet<>(block));
    }

    private boolean matchExact(List<String> seq, int index, List<String> block) {
        if (index + block.size() > seq.size()) return false;
        for (int i = 0; i < block.size(); i++) {
            if (!seq.get(index + i).equals(block.get(i))) return false;
        }
        return true;
    }

    // ====================================================
    // LG7 / LG8 / LG11 / LG12 (unchanged core logic)
    // ====================================================
    private String detectSeriesLG7(List<String> seq) {
        if (seq.size() < 4) return null;
        List<String> start = seq.subList(0, 4);
        if (start.contains("RS6")) return "SERIES1";
        if (start.contains("RS16")) return "SERIES2";
        return null;
    }

    private boolean validateLG7(List<String> seq, Map<String, List<String>> blocks, String series) {
        int index = 0;

        if (!matchFlexible(seq, index, blocks.get("START"))) return false;
        index += blocks.get("START").size();

        if (series.equals("SERIES1")) {
            if (!matchLG7Series1Middle(seq, index)) return false;
            index += 5;
        } else {
            if (!matchExact(seq, index, blocks.get("MIDDLE"))) return false;
            index += blocks.get("MIDDLE").size();
        }

        if (!matchLG7End(seq, index)) return false;
        return true;
    }

    private boolean matchLG7Series1Middle(List<String> seq, int index) {
        if (index + 5 > seq.size()) return false;

        String a = seq.get(index);
        String x = seq.get(index + 1);
        String y = seq.get(index + 2);
        String b = seq.get(index + 3);
        String c = seq.get(index + 4);

        if (!a.equals("RS8") || !b.equals("RS8") || !c.equals("RS14")) return false;
        return Set.of(x, y).equals(Set.of("RS6", "RS16"));
    }

    private boolean matchLG7End(List<String> seq, int index) {
        if (index + 5 > seq.size()) return false;

        String A = seq.get(index);
        String mid = seq.get(index + 1);
        String B = seq.get(index + 2);
        String C = seq.get(index + 3);
        String last = seq.get(index + 4);

        if (!mid.equals("RS8") || !last.equals("RS8")) return false;
        if (!(A.equals("RS4") || A.equals("RS12"))) return false;
        return Set.of(B, C).equals(Set.of(A, "RS16"));
    }

    private String detectSeriesLG8(List<String> seq) {
        if (seq.size() < 4) return null;
        List<String> start = seq.subList(0, 4);
        if (start.contains("RS6")) return "SERIES1";
        if (start.contains("RS16")) return "SERIES2";
        return null;
    }

    private boolean validateLG8(List<String> seq, Map<String, List<String>> blocks, String series) {
        int index = 0;

        if (!matchFlexible(seq, index, blocks.get("START"))) return false;
        index += blocks.get("START").size();

        if (series.equals("SERIES1")) {
            if (!matchExact(seq, index, blocks.get("MIDDLE"))) return false;
            index += blocks.get("MIDDLE").size();
        } else {
            if (!matchLG8Series2Middle(seq, index)) return false;
            index += 5;
        }

        if (!matchLG8End(seq, index, blocks.get("END"))) return false;
        return true;
    }

    private boolean matchLG8Series2Middle(List<String> seq, int index) {
        if (index + 5 > seq.size()) return false;

        return seq.get(index).equals("RS8")
                && seq.get(index + 3).equals("RS8")
                && seq.get(index + 4).equals("RS5")
                && Set.of(seq.get(index + 1), seq.get(index + 2)).equals(Set.of("RS6", "RS16"));
    }

    private boolean matchLG8End(List<String> seq, int index, List<String> end) {
        if (index + 5 > seq.size()) return false;

        String A1 = seq.get(index);
        String mid = seq.get(index + 1);
        String A2 = seq.get(index + 2);
        String B = seq.get(index + 3);
        String last = seq.get(index + 4);

        if (!mid.equals("RS8") || !last.equals("RS8")) return false;
        if (!(A1.equals("RS4") || A1.equals("RS12"))) return false;

        return (A2.equals(A1) && B.equals("RS6"))
                || (A2.equals("RS6") && B.equals(A1));
    }

    private String detectSeriesLG11(List<String> seq) {
        if (seq.size() < 4) return null;
        List<String> start = seq.subList(0, 4);
        if (start.contains("RS6")) return "SERIES1";
        if (start.contains("RS16")) return "SERIES2";
        return null;
    }

    private boolean validateLG11(List<String> seq, Map<String, List<String>> blocks, String series) {
        int index = 0;

        if (!matchFlexible(seq, index, blocks.get("START"))) return false;
        index += blocks.get("START").size();

        if (series.equals("SERIES1")) {
            if (!matchExact(seq, index, blocks.get("MIDDLE"))) return false;
            index += blocks.get("MIDDLE").size();
        } else {
            if (!matchLG11Series2Middle(seq, index)) return false;
            index += 5;
        }

        return matchLG11End(seq, index);
    }

    private boolean matchLG11Series2Middle(List<String> seq, int index) {
        if (index + 5 > seq.size()) return false;

        return seq.get(index).equals("RS8")
                && seq.get(index + 3).equals("RS8")
                && seq.get(index + 4).equals("RS5")
                && Set.of(seq.get(index + 1), seq.get(index + 2)).equals(Set.of("RS6", "RS16"));
    }

    private boolean matchLG11End(List<String> seq, int index) {
        int rem = seq.size() - index;

        if (rem == 1)
            return seq.get(index).equals("RS4") || seq.get(index).equals("RS12");

        if (rem == 2) {
            String a = seq.get(index);
            String b = seq.get(index + 1);

            if (!(a.equals("RS4") || a.equals("RS12"))) return false;
            if (!(b.equals("RS4") || b.equals("RS12"))) return false;

            return !(a.equals("RS12") && b.equals("RS12"));
        }
        return false;
    }

    private String detectSeriesLG12(List<String> seq) {
        if (seq.size() < 4) return null;
        List<String> start = seq.subList(0, 4);
        if (start.contains("RS6")) return "SERIES1";
        if (start.contains("RS16")) return "SERIES2";
        return null;
    }

    private boolean validateLG12(List<String> seq, String series) {
        int index = 0;

        if (series.equals("SERIES1")) {
            if (!matchFlexible(seq, index, Arrays.asList("RS8", "RS6", "RS4", "RS12")))
                return false;
        } else {
            if (!matchFlexible(seq, index, Arrays.asList("RS8", "RS16", "RS4", "RS12")))
                return false;
        }
        index += 4;

        if (series.equals("SERIES1")) {
            if (!matchLG12Series1Middle(seq, index)) return false;
            index += 5;
        } else {
            if (!matchLG12Series2Middle(seq, index)) return false;
            index += 2;
        }

        return matchLG12End(seq, index);
    }

    private boolean matchLG12Series1Middle(List<String> seq, int index) {
        if (index + 5 > seq.size()) return false;

        return seq.get(index).equals("RS8")
                && seq.get(index + 3).equals("RS8")
                && seq.get(index + 4).equals("RS14")
                && Set.of(seq.get(index + 1), seq.get(index + 2)).equals(Set.of("RS6", "RS16"));
    }

    private boolean matchLG12Series2Middle(List<String> seq, int index) {
        if (index + 2 > seq.size()) return false;
        return seq.get(index).equals("RS8") && seq.get(index + 1).equals("RS14");
    }

    private boolean matchLG12End(List<String> seq, int index) {
        int rem = seq.size() - index;

        if (rem == 1)
            return seq.get(index).equals("RS4") || seq.get(index).equals("RS12");

        if (rem == 2) {
            String a = seq.get(index);
            String b = seq.get(index + 1);

            if (!(a.equals("RS4") || a.equals("RS12"))) return false;
            if (!(b.equals("RS4") || b.equals("RS12"))) return false;

            return !(a.equals("RS12") && b.equals("RS12"));
        }
        return false;
    }
}
