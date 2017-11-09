package com.skidata.wimc.tracking.impl;

import com.skidata.wimc.tracking.Camera;

public class MappingContext {
    public Camera cam;
    public long lpWidth;
    public long lpHeight1;
    public long lpHeight2;
    public String lp;

    public MappingContext(Camera cam, long lpWidth, long lpHeight1, long lpHeight2, String lp) {
        this.cam = cam;
        this.lpWidth = lpWidth;
        this.lpHeight1 = lpHeight1;
        this.lpHeight2 = lpHeight2;
        this.lp = lp;
    }


}
