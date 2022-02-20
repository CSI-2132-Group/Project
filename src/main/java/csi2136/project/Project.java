package csi2136.project;

import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme;
import csi2136.project.ui.ServerFrame;

public class Project {

    static {
        FlatDarkFlatIJTheme.setup();
    }

    public static void main(String[] args) {
        ServerFrame.create();
    }

}
