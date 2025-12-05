package com.tms.transport_management_system.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadWithBidsDTO {

    private LoadDTO load;
    private List<BidDTO> activeBids;

}
