package com.scada.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class CanvasData 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
    @Lob
    @Column(name ="canvas_data",columnDefinition = "LONGTEXT")
	private String canvasData;
    public String getCanvasJson() {
        return canvasData;
    }

    public void setCanvasJson(String canvasJson) {
        this.canvasData = canvasJson;
    }
}
