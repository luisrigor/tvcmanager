package com.gsc.tvcmanager.model.toyota.entity;

import lombok.*;
import java.util.Hashtable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrevisionFilterBean {

        private String oidNet;
        private String oidDealer;
        private int year;
        private int month;
        private String orderColumn;
        private String orderOrientation;
        private boolean validOpenMonth;
        private Hashtable<Integer, String> hstMonths;
        private int actualYear;
        private int actualMonth;
}
