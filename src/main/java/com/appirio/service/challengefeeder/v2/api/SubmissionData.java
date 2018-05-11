package com.appirio.service.challengefeeder.v2.api;

import java.util.Date;

import com.appirio.service.challengefeeder.api.IdentifiableData;
import com.appirio.service.challengefeeder.helper.CustomDateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubmissionData extends IdentifiableData{

	 /**
     * The status field
     */
    private String status;
    
    
    private Double score;
    
    /**
     * The placement field
     */
    private Integer placement;
    
    
    private String submissionImage;
    
    
    /**
     * The submissionTime field
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date submissionTime;
    
    private String handle;
    
    private Double screeningScore;
    
    private Double initialScore;
    
    private Double finalScore;
    
    private Double points;
    
    private String submissionStatus;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date submissionDate;
    
    private Long submissionId;
    
    private String submitter;
    
    @JsonIgnore
    private Long challengeId;
    
    @JsonIgnore
    private Double digitalRunPoints;
    
}
