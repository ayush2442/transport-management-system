package com.tms.transport_management_system.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BestBidDTO {

    private BidDTO bid;
    private Double score;
    private Double transporterRating;

}
