package com.scada.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FaultConfig {

    // 🔥 Flexible sequences block storage
    public static Map<String, Map<String, List<String>>> flex = new HashMap<>();

    static {
    	
    	flex.put("LG7_SERIES1", Map.of(
    	    "START",  List.of("RS8", "RS6", "RS4", "RS12"),
    	    "MIDDLE", List.of("RS8", "RS6", "RS16", "RS8", "RS14"),
    	    "END",    List.of("RS4", "RS8", "RS4", "RS16", "RS8")
    	));

    flex.put("LG7_SERIES2", Map.of(
    	    "START",  List.of("RS8", "RS16", "RS4", "RS12"),

    	    "MIDDLE", List.of("RS8", "RS14"),

    	    "END",    List.of("RS4", "RS8", "RS4", "RS16", "RS8")
    	));


        // ------------------- LG8 --------------------
        flex.put("LG8_SERIES1", Map.of(
        	    "START",  List.of("RS8", "RS6", "RS4", "RS12"),
        	    "MIDDLE", List.of("RS8", "RS5"),
        	    "END",    List.of("RS4", "RS8", "RS4", "RS6", "RS8")
        	));


        flex.put("LG8_SERIES2", Map.of(
        	    "START",  List.of("RS8", "RS16", "RS4", "RS12"),
        	    "MIDDLE", List.of("RS8", "RS6", "RS16", "RS8", "RS5"), 
        	    "END",    List.of("RS4", "RS8", "RS4", "RS6", "RS8")
        	));


        // ------------------- LG11 --------------------
     // ------------------- LG11 --------------------
        flex.put("LG11_SERIES1", Map.of(
                "START",  List.of("RS8", "RS6", "RS4", "RS12"),   // flexible

                // Fixed MIDDLE pattern
                "MIDDLE", List.of("RS8", "RS5"),  

                // END is flexible
                "END",    List.of("RS4", "RS12")   // service handles combinations
        ));


        flex.put("LG11_SERIES2", Map.of(
                "START",  List.of("RS8", "RS16", "RS4", "RS12"),   // flexible detection

                // Pattern: RS8, (RS6 or RS16), (RS6 or RS16), RS8, RS5
                "MIDDLE", List.of("RS8", "RS16", "RS6", "RS8", "RS5"),

                // END flexible 
                "END",    List.of("RS4", "RS12")
        ));


     // ------------------- LG12 --------------------
        flex.put("LG12_SERIES1", Map.of(
            "START", List.of("RS8", "RS6", "RS4", "RS12"),  // flexible block

            // Middle = RS8, RS6/RS16, RS16/RS6, RS8, RS14
            // but FIX stores only template markers
            "MIDDLE", List.of("RS8", "RS6", "RS16", "RS8", "RS14"),

            // END: A or A,B combinations handled in service
            "END", List.of("RS4", "RS12")
        ));

        flex.put("LG12_SERIES2", Map.of(
            "START", List.of("RS8", "RS16", "RS4", "RS12"),  // flexible block

            // Middle = RS8, RS14
            "MIDDLE", List.of("RS8", "RS14"),

            // END: same flexible rule
            "END", List.of("RS4", "RS12")
        ));

     // ------------------- LG24 RS17 --------------------
        flex.put("LG24_RS17", Map.of(
                "START",  List.of("RS18", "RS17", "RS38", "RS26", "RS45"),
                "MIDDLE", List.of("RS18", "RS15", "RS17"),
                "END",    List.of("RS18", "RS37", "RS38", "RS25", "RS26")  
                // Service will allow RS45 instead of RS38 + extras
        ));

        // ------------------- LG24 RS15 --------------------
        flex.put("LG24_RS15", Map.of(
                "START",  List.of("RS18", "RS15", "RS38", "RS26", "RS45"),
                "MIDDLE", List.of(),   // no middle
                "END",    List.of("RS18", "RS37", "RS38", "RS25", "RS26")
        ));

        flex.put("LG25_RS15", Map.of(
                
        	    // START → RS18 FIRST + set of 4 signals (any order)
        	    "START",  List.of("RS18", "RS15", "RS26", "RS38", "RS45"),

        	    // MIDDLE → RS18 → {RS15,RS17} any order → RS18
        	    // We store template shape:
        	    "MIDDLE", List.of("RS18", "RS15", "RS17", "RS18"),

        	    // END → RS39 → (RS38/RS45) → RS27 → RS26 → optional tail
        	    // We store mandatory core only
        	    "END",    List.of("RS39", "RS38", "RS27", "RS26")
        	));

        flex.put("LG25_RS17", Map.of(
                
        	    // START → RS18 FIRST + set of 4 signals (any order)
        	    "START",  List.of("RS18", "RS17", "RS26", "RS38", "RS45"),

        	    // MIDDLE → RS18 → {RS15,RS17} any order → RS18
        	    "MIDDLE", List.of(),

        	    // END → SAME as RS15 variant (core mandatory)
        	    "END",    List.of("RS39", "RS38", "RS27", "RS26")
        	));

        flex.put("LG38_RS15", Map.of(

        	    // START: RS18 first + {RS15, RS26, RS38, RS45} in ANY order
        	    "START",  List.of("RS18", "RS15", "RS26", "RS38", "RS45"),

        	    // MIDDLE: ordered → RS18, RS37, (RS38/RS45), RS25, RS26
        	    // But config only stores the template, service applies rules
        	    "MIDDLE", List.of("RS18", "RS37", "RS38", "RS25", "RS26"),

        	    // END: fixed first 2 (RS18, RS26)
        	    // Tail is validated in service: {RS15, RS18, optional RS45}
        	    "END",    List.of("RS18", "RS26")
        	));

        flex.put("LG38_RS17", Map.of(

        	    // START: RS18 first + {RS17, RS26, RS38, RS45} ANY ORDER
        	    "START",  List.of("RS18", "RS17", "RS26", "RS38", "RS45"),

        	    // MIDDLE: ordered (RS17 + RS15 flex is handled in service)
        	    "MIDDLE", List.of("RS18", "RS37", "RS38", "RS25", "RS26"),

        	    // END: core only (RS18, RS26) → tail flex in service
        	    "END",    List.of("RS18", "RS26")
        	));

        flex.put("LG39_RS15", Map.of(
        	    "START",  List.of("RS18", "RS15", "RS26", "RS38", "RS45"),
        	    // MIDDLE for RS15: RS18 → RS39 → (RS38/RS45) → RS27 → RS26
        	    "MIDDLE", List.of("RS18", "RS39", "RS38", "RS27", "RS26"),
        	    // END fixed part (tail handled in service)
        	    "END",    List.of("RS18", "RS26")
        	));

        flex.put("LG39_RS17", Map.of(
        	    "START",  List.of("RS18", "RS17", "RS26", "RS38", "RS45"),
        	    "MIDDLE", List.of("RS18", "RS39", "RS38", "RS27", "RS26"),
        	    "END",    List.of("RS18", "RS26")
        	));
        flex.put("LG45_RS17", Map.of(
        	    "START",  List.of("RS18", "RS17", "RS26", "RS38", "RS45"),
        	    "MIDDLE", List.of("RS17","RS15","RS18","RS37"),  // service applies flexible rule
        	    "END",    List.of("RS18","RS26")                // service handles tail flex
        	));

        	flex.put("LG45_RS15", Map.of(
        	    "START",  List.of("RS18", "RS15", "RS26", "RS38", "RS45"),
        	    "MIDDLE", List.of("RS18","RS37"),              // service handles X (RS38/RS45)
        	    "END",    List.of("RS18","RS26")
        	));

        	flex.put("LG46_RS15", Map.of(
        		    "START",  List.of("RS18", "RS15", "RS26", "RS38", "RS45"),   // RS18 must be first (service checks)
        		    "MIDDLE", List.of("RS18", "RS39"),                          // service handles pair {RS17,RS15} + X
        		    "END",    List.of("RS18")                                   // service handles full END logic
        		));

        	flex.put("LG46_RS17", Map.of(
        		    "START",  List.of("RS18", "RS17", "RS26", "RS38", "RS45"),
        		    "MIDDLE", List.of("RS18", "RS39"),      // last part (X) validated by service
        		    "END",    List.of("RS18")               // service handles full END logic
        		));

    }



    public static Map<String, List<String>> getFlexible(String line, String variant) {
        return flex.get(line + "_" + variant);
    }
}
