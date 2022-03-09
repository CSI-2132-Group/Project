package csi2136.project;

import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme;
import csi2136.project.ui.ServerFrame;

import java.awt.*;

public class Project {

    static {
        FlatDarkFlatIJTheme.setup();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> ServerFrame.create());
    }

}
