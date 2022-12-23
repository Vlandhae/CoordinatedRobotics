package com.application.base.layout.center;

import com.application.base.layout.robocontroller.RobotControllerController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoomController {

    private String taken = "#282828";
    private String untaken = "#ffffff";
    public List<CoordinatPair> freeFields;
    public List<CoordinatPair> unfreeFields;

    @FXML
    public GridPane gridPane;

    public RoomController() {
        this.freeFields = new ArrayList<>();
        this.unfreeFields = new ArrayList<>();
        this.exampleOne();
    }

    private void exampleOne() {
        freeFields.add(new CoordinatPair(10, 10));
        freeFields.add(new CoordinatPair(10, 11));
        freeFields.add(new CoordinatPair(10, 12));
        freeFields.add(new CoordinatPair(10, 13));
        freeFields.add(new CoordinatPair(10, 14));
        freeFields.add(new CoordinatPair(11, 10));
        freeFields.add(new CoordinatPair(11, 11));
        freeFields.add(new CoordinatPair(11, 12));
        freeFields.add(new CoordinatPair(11, 13));
        freeFields.add(new CoordinatPair(11, 14));
        freeFields.add(new CoordinatPair(12, 10));
        freeFields.add(new CoordinatPair(12, 11));
        freeFields.add(new CoordinatPair(12, 12));
        freeFields.add(new CoordinatPair(12, 13));
        freeFields.add(new CoordinatPair(12, 14));
        freeFields.add(new CoordinatPair(13, 10));
        freeFields.add(new CoordinatPair(13, 11));
        freeFields.add(new CoordinatPair(13, 12));
        freeFields.add(new CoordinatPair(13, 13));
        freeFields.add(new CoordinatPair(13, 14));
        freeFields.add(new CoordinatPair(14, 10));
        freeFields.add(new CoordinatPair(14, 11));
        freeFields.add(new CoordinatPair(14, 12));
        freeFields.add(new CoordinatPair(14, 13));
        freeFields.add(new CoordinatPair(14, 14));

        freeFields.add(new CoordinatPair(15, 13));
        freeFields.add(new CoordinatPair(16, 13));
        freeFields.add(new CoordinatPair(13, 15));
        freeFields.add(new CoordinatPair(13, 16));
        freeFields.add(new CoordinatPair(14, 15));
        freeFields.add(new CoordinatPair(14, 16));
        freeFields.add(new CoordinatPair(15, 15));
        freeFields.add(new CoordinatPair(15, 16));
        freeFields.add(new CoordinatPair(16, 14));
        freeFields.add(new CoordinatPair(16, 15));
        freeFields.add(new CoordinatPair(16, 16));
        freeFields.add(new CoordinatPair(15, 14));

        unfreeFields.add(new CoordinatPair(9,9));
        unfreeFields.add(new CoordinatPair(9,10));
        unfreeFields.add(new CoordinatPair(9,11));
        unfreeFields.add(new CoordinatPair(9,12));
        unfreeFields.add(new CoordinatPair(9,13));
        unfreeFields.add(new CoordinatPair(9,14));
        unfreeFields.add(new CoordinatPair(9,15));

        unfreeFields.add(new CoordinatPair(15,9));
        unfreeFields.add(new CoordinatPair(15,10));
        unfreeFields.add(new CoordinatPair(15,11));
        unfreeFields.add(new CoordinatPair(15,12));
        unfreeFields.add(new CoordinatPair(16,12));
        unfreeFields.add(new CoordinatPair(17,12));
        unfreeFields.add(new CoordinatPair(17,13));
        unfreeFields.add(new CoordinatPair(17,14));
        unfreeFields.add(new CoordinatPair(17,15));
        unfreeFields.add(new CoordinatPair(17,16));
        unfreeFields.add(new CoordinatPair(17,17));
        unfreeFields.add(new CoordinatPair(16,17));
        unfreeFields.add(new CoordinatPair(15,17));
        unfreeFields.add(new CoordinatPair(14,17));
        unfreeFields.add(new CoordinatPair(13,17));
        unfreeFields.add(new CoordinatPair(12,17));
        unfreeFields.add(new CoordinatPair(12,16));


        unfreeFields.add(new CoordinatPair(9,9));
        unfreeFields.add(new CoordinatPair(10,9));
        unfreeFields.add(new CoordinatPair(11,9));
        unfreeFields.add(new CoordinatPair(12,9));
        unfreeFields.add(new CoordinatPair(13,9));
        unfreeFields.add(new CoordinatPair(14,9));
        unfreeFields.add(new CoordinatPair(12,12));
        unfreeFields.add(new CoordinatPair(11,11));
        unfreeFields.add(new CoordinatPair(11,12));
        unfreeFields.add(new CoordinatPair(12,11));

        unfreeFields.add(new CoordinatPair(9,15));
        unfreeFields.add(new CoordinatPair(10,15));
        unfreeFields.add(new CoordinatPair(11,15));
        unfreeFields.add(new CoordinatPair(12,15));

    }

    public void load() {
        FXMLLoader fxmlLoader = new FXMLLoader(RobotControllerController.class.getResource("/com.application.base.layout/center/room.fxml"));
        fxmlLoader.setController(this);
        try {
            gridPane = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        doColor();
    }

    public void doColor() {
        for (CoordinatPair coordinatPair : freeFields) {
            StackPane pane = this.getStackPane(coordinatPair.x(), coordinatPair.y());
            if (pane == null) {
                continue;
            }
            pane.setStyle("-fx-background-color: " + untaken + ";");
        }
        for (CoordinatPair coordinatPair : unfreeFields) {
            StackPane pane = this.getStackPane(coordinatPair.x(), coordinatPair.y());
            if (pane == null) {
                continue;
            }
            pane.setStyle("-fx-background-color: " + taken + ";");
        }
    }

    // region stackPanes
    @FXML public StackPane a1;
    @FXML public StackPane a2;
    @FXML public StackPane a3;
    @FXML public StackPane a4;
    @FXML public StackPane a5;
    @FXML public StackPane a6;
    @FXML public StackPane a7;
    @FXML public StackPane a8;
    @FXML public StackPane a9;
    @FXML public StackPane a10;
    @FXML public StackPane a11;
    @FXML public StackPane a12;
    @FXML public StackPane a13;
    @FXML public StackPane a14;
    @FXML public StackPane a15;
    @FXML public StackPane a16;
    @FXML public StackPane a17;
    @FXML public StackPane a18;
    @FXML public StackPane a19;
    @FXML public StackPane a20;
    @FXML public StackPane b1;
    @FXML public StackPane b2;
    @FXML public StackPane b3;
    @FXML public StackPane b4;
    @FXML public StackPane b5;
    @FXML public StackPane b6;
    @FXML public StackPane b7;
    @FXML public StackPane b8;
    @FXML public StackPane b9;
    @FXML public StackPane b10;
    @FXML public StackPane b11;
    @FXML public StackPane b12;
    @FXML public StackPane b13;
    @FXML public StackPane b14;
    @FXML public StackPane b15;
    @FXML public StackPane b16;
    @FXML public StackPane b17;
    @FXML public StackPane b18;
    @FXML public StackPane b19;
    @FXML public StackPane b20;
    @FXML public StackPane c1;
    @FXML public StackPane c2;
    @FXML public StackPane c3;
    @FXML public StackPane c4;
    @FXML public StackPane c5;
    @FXML public StackPane c6;
    @FXML public StackPane c7;
    @FXML public StackPane c8;
    @FXML public StackPane c9;
    @FXML public StackPane c10;
    @FXML public StackPane c11;
    @FXML public StackPane c12;
    @FXML public StackPane c13;
    @FXML public StackPane c14;
    @FXML public StackPane c15;
    @FXML public StackPane c16;
    @FXML public StackPane c17;
    @FXML public StackPane c18;
    @FXML public StackPane c19;
    @FXML public StackPane c20;
    @FXML public StackPane d1;
    @FXML public StackPane d2;
    @FXML public StackPane d3;
    @FXML public StackPane d4;
    @FXML public StackPane d5;
    @FXML public StackPane d6;
    @FXML public StackPane d7;
    @FXML public StackPane d8;
    @FXML public StackPane d9;
    @FXML public StackPane d10;
    @FXML public StackPane d11;
    @FXML public StackPane d12;
    @FXML public StackPane d13;
    @FXML public StackPane d14;
    @FXML public StackPane d15;
    @FXML public StackPane d16;
    @FXML public StackPane d17;
    @FXML public StackPane d18;
    @FXML public StackPane d19;
    @FXML public StackPane d20;
    @FXML public StackPane e1;
    @FXML public StackPane e2;
    @FXML public StackPane e3;
    @FXML public StackPane e4;
    @FXML public StackPane e5;
    @FXML public StackPane e6;
    @FXML public StackPane e7;
    @FXML public StackPane e8;
    @FXML public StackPane e9;
    @FXML public StackPane e10;
    @FXML public StackPane e11;
    @FXML public StackPane e12;
    @FXML public StackPane e13;
    @FXML public StackPane e14;
    @FXML public StackPane e15;
    @FXML public StackPane e16;
    @FXML public StackPane e17;
    @FXML public StackPane e18;
    @FXML public StackPane e19;
    @FXML public StackPane e20;
    @FXML public StackPane f1;
    @FXML public StackPane f2;
    @FXML public StackPane f3;
    @FXML public StackPane f4;
    @FXML public StackPane f5;
    @FXML public StackPane f6;
    @FXML public StackPane f7;
    @FXML public StackPane f8;
    @FXML public StackPane f9;
    @FXML public StackPane f10;
    @FXML public StackPane f11;
    @FXML public StackPane f12;
    @FXML public StackPane f13;
    @FXML public StackPane f14;
    @FXML public StackPane f15;
    @FXML public StackPane f16;
    @FXML public StackPane f17;
    @FXML public StackPane f18;
    @FXML public StackPane f19;
    @FXML public StackPane f20;
    @FXML public StackPane g1;
    @FXML public StackPane g2;
    @FXML public StackPane g3;
    @FXML public StackPane g4;
    @FXML public StackPane g5;
    @FXML public StackPane g6;
    @FXML public StackPane g7;
    @FXML public StackPane g8;
    @FXML public StackPane g9;
    @FXML public StackPane g10;
    @FXML public StackPane g11;
    @FXML public StackPane g12;
    @FXML public StackPane g13;
    @FXML public StackPane g14;
    @FXML public StackPane g15;
    @FXML public StackPane g16;
    @FXML public StackPane g17;
    @FXML public StackPane g18;
    @FXML public StackPane g19;
    @FXML public StackPane g20;
    @FXML public StackPane h1;
    @FXML public StackPane h2;
    @FXML public StackPane h3;
    @FXML public StackPane h4;
    @FXML public StackPane h5;
    @FXML public StackPane h6;
    @FXML public StackPane h7;
    @FXML public StackPane h8;
    @FXML public StackPane h9;
    @FXML public StackPane h10;
    @FXML public StackPane h11;
    @FXML public StackPane h12;
    @FXML public StackPane h13;
    @FXML public StackPane h14;
    @FXML public StackPane h15;
    @FXML public StackPane h16;
    @FXML public StackPane h17;
    @FXML public StackPane h18;
    @FXML public StackPane h19;
    @FXML public StackPane h20;
    @FXML public StackPane i1;
    @FXML public StackPane i2;
    @FXML public StackPane i3;
    @FXML public StackPane i4;
    @FXML public StackPane i5;
    @FXML public StackPane i6;
    @FXML public StackPane i7;
    @FXML public StackPane i8;
    @FXML public StackPane i9;
    @FXML public StackPane i10;
    @FXML public StackPane i11;
    @FXML public StackPane i12;
    @FXML public StackPane i13;
    @FXML public StackPane i14;
    @FXML public StackPane i15;
    @FXML public StackPane i16;
    @FXML public StackPane i17;
    @FXML public StackPane i18;
    @FXML public StackPane i19;
    @FXML public StackPane i20;
    @FXML public StackPane j1;
    @FXML public StackPane j2;
    @FXML public StackPane j3;
    @FXML public StackPane j4;
    @FXML public StackPane j5;
    @FXML public StackPane j6;
    @FXML public StackPane j7;
    @FXML public StackPane j8;
    @FXML public StackPane j9;
    @FXML public StackPane j10;
    @FXML public StackPane j11;
    @FXML public StackPane j12;
    @FXML public StackPane j13;
    @FXML public StackPane j14;
    @FXML public StackPane j15;
    @FXML public StackPane j16;
    @FXML public StackPane j17;
    @FXML public StackPane j18;
    @FXML public StackPane j19;
    @FXML public StackPane j20;
    @FXML public StackPane k1;
    @FXML public StackPane k2;
    @FXML public StackPane k3;
    @FXML public StackPane k4;
    @FXML public StackPane k5;
    @FXML public StackPane k6;
    @FXML public StackPane k7;
    @FXML public StackPane k8;
    @FXML public StackPane k9;
    @FXML public StackPane k10;
    @FXML public StackPane k11;
    @FXML public StackPane k12;
    @FXML public StackPane k13;
    @FXML public StackPane k14;
    @FXML public StackPane k15;
    @FXML public StackPane k16;
    @FXML public StackPane k17;
    @FXML public StackPane k18;
    @FXML public StackPane k19;
    @FXML public StackPane k20;
    @FXML public StackPane l1;
    @FXML public StackPane l2;
    @FXML public StackPane l3;
    @FXML public StackPane l4;
    @FXML public StackPane l5;
    @FXML public StackPane l6;
    @FXML public StackPane l7;
    @FXML public StackPane l8;
    @FXML public StackPane l9;
    @FXML public StackPane l10;
    @FXML public StackPane l11;
    @FXML public StackPane l12;
    @FXML public StackPane l13;
    @FXML public StackPane l14;
    @FXML public StackPane l15;
    @FXML public StackPane l16;
    @FXML public StackPane l17;
    @FXML public StackPane l18;
    @FXML public StackPane l19;
    @FXML public StackPane l20;
    @FXML public StackPane m1;
    @FXML public StackPane m2;
    @FXML public StackPane m3;
    @FXML public StackPane m4;
    @FXML public StackPane m5;
    @FXML public StackPane m6;
    @FXML public StackPane m7;
    @FXML public StackPane m8;
    @FXML public StackPane m9;
    @FXML public StackPane m10;
    @FXML public StackPane m11;
    @FXML public StackPane m12;
    @FXML public StackPane m13;
    @FXML public StackPane m14;
    @FXML public StackPane m15;
    @FXML public StackPane m16;
    @FXML public StackPane m17;
    @FXML public StackPane m18;
    @FXML public StackPane m19;
    @FXML public StackPane m20;
    @FXML public StackPane n1;
    @FXML public StackPane n2;
    @FXML public StackPane n3;
    @FXML public StackPane n4;
    @FXML public StackPane n5;
    @FXML public StackPane n6;
    @FXML public StackPane n7;
    @FXML public StackPane n8;
    @FXML public StackPane n9;
    @FXML public StackPane n10;
    @FXML public StackPane n11;
    @FXML public StackPane n12;
    @FXML public StackPane n13;
    @FXML public StackPane n14;
    @FXML public StackPane n15;
    @FXML public StackPane n16;
    @FXML public StackPane n17;
    @FXML public StackPane n18;
    @FXML public StackPane n19;
    @FXML public StackPane n20;
    @FXML public StackPane o1;
    @FXML public StackPane o2;
    @FXML public StackPane o3;
    @FXML public StackPane o4;
    @FXML public StackPane o5;
    @FXML public StackPane o6;
    @FXML public StackPane o7;
    @FXML public StackPane o8;
    @FXML public StackPane o9;
    @FXML public StackPane o10;
    @FXML public StackPane o11;
    @FXML public StackPane o12;
    @FXML public StackPane o13;
    @FXML public StackPane o14;
    @FXML public StackPane o15;
    @FXML public StackPane o16;
    @FXML public StackPane o17;
    @FXML public StackPane o18;
    @FXML public StackPane o19;
    @FXML public StackPane o20;
    @FXML public StackPane p1;
    @FXML public StackPane p2;
    @FXML public StackPane p3;
    @FXML public StackPane p4;
    @FXML public StackPane p5;
    @FXML public StackPane p6;
    @FXML public StackPane p7;
    @FXML public StackPane p8;
    @FXML public StackPane p9;
    @FXML public StackPane p10;
    @FXML public StackPane p11;
    @FXML public StackPane p12;
    @FXML public StackPane p13;
    @FXML public StackPane p14;
    @FXML public StackPane p15;
    @FXML public StackPane p16;
    @FXML public StackPane p17;
    @FXML public StackPane p18;
    @FXML public StackPane p19;
    @FXML public StackPane p20;
    @FXML public StackPane q1;
    @FXML public StackPane q2;
    @FXML public StackPane q3;
    @FXML public StackPane q4;
    @FXML public StackPane q5;
    @FXML public StackPane q6;
    @FXML public StackPane q7;
    @FXML public StackPane q8;
    @FXML public StackPane q9;
    @FXML public StackPane q10;
    @FXML public StackPane q11;
    @FXML public StackPane q12;
    @FXML public StackPane q13;
    @FXML public StackPane q14;
    @FXML public StackPane q15;
    @FXML public StackPane q16;
    @FXML public StackPane q17;
    @FXML public StackPane q18;
    @FXML public StackPane q19;
    @FXML public StackPane q20;
    @FXML public StackPane r1;
    @FXML public StackPane r2;
    @FXML public StackPane r3;
    @FXML public StackPane r4;
    @FXML public StackPane r5;
    @FXML public StackPane r6;
    @FXML public StackPane r7;
    @FXML public StackPane r8;
    @FXML public StackPane r9;
    @FXML public StackPane r10;
    @FXML public StackPane r11;
    @FXML public StackPane r12;
    @FXML public StackPane r13;
    @FXML public StackPane r14;
    @FXML public StackPane r15;
    @FXML public StackPane r16;
    @FXML public StackPane r17;
    @FXML public StackPane r18;
    @FXML public StackPane r19;
    @FXML public StackPane r20;
    @FXML public StackPane s1;
    @FXML public StackPane s2;
    @FXML public StackPane s3;
    @FXML public StackPane s4;
    @FXML public StackPane s5;
    @FXML public StackPane s6;
    @FXML public StackPane s7;
    @FXML public StackPane s8;
    @FXML public StackPane s9;
    @FXML public StackPane s10;
    @FXML public StackPane s11;
    @FXML public StackPane s12;
    @FXML public StackPane s13;
    @FXML public StackPane s14;
    @FXML public StackPane s15;
    @FXML public StackPane s16;
    @FXML public StackPane s17;
    @FXML public StackPane s18;
    @FXML public StackPane s19;
    @FXML public StackPane s20;
    @FXML public StackPane t1;
    @FXML public StackPane t2;
    @FXML public StackPane t3;
    @FXML public StackPane t4;
    @FXML public StackPane t5;
    @FXML public StackPane t6;
    @FXML public StackPane t7;
    @FXML public StackPane t8;
    @FXML public StackPane t9;
    @FXML public StackPane t10;
    @FXML public StackPane t11;
    @FXML public StackPane t12;
    @FXML public StackPane t13;
    @FXML public StackPane t14;
    @FXML public StackPane t15;
    @FXML public StackPane t16;
    @FXML public StackPane t17;
    @FXML public StackPane t18;
    @FXML public StackPane t19;
    @FXML public StackPane t20;
    //endregion

    // region getStackPane methods
    public StackPane getStackPane(int x, int y) {
        switch (x) {
            case 1 : return getStackA(y);
            case 2 : return getStackB(y);
            case 3 : return getStackC(y);
            case 4 : return getStackD(y);
            case 5 : return getStackE(y);
            case 6 : return getStackF(y);
            case 7 : return getStackG(y);
            case 8 : return getStackH(y);
            case 9 : return getStackI(y);
            case 10 : return getStackJ(y);
            case 11 : return getStackK(y);
            case 12 : return getStackL(y);
            case 13 : return getStackM(y);
            case 14 : return getStackN(y);
            case 15 : return getStackO(y);
            case 16 : return getStackP(y);
            case 17 : return getStackQ(y);
            case 18 : return getStackR(y);
            case 19 : return getStackS(y);
            case 20 : return getStackT(y);
            default: return null;
        }
    }

    private StackPane getStackA(int y) {
        switch (y) {
            case 1 : return a1;
            case 2 : return a2;
            case 3 : return a3;
            case 4 : return a4;
            case 5 : return a5;
            case 6 : return a6;
            case 7 : return a7;
            case 8 : return a8;
            case 9 : return a9;
            case 10 : return a10;
            case 11 : return a11;
            case 12 : return a12;
            case 13 : return a13;
            case 14 : return a14;
            case 15 : return a15;
            case 16 : return a16;
            case 17 : return a17;
            case 18 : return a18;
            case 19 : return a19;
            case 20 : return a20;
            default: return null;
        }
    }

    private StackPane getStackB(int y) {
        switch (y) {
            case 1 : return b1;
            case 2 : return b2;
            case 3 : return b3;
            case 4 : return b4;
            case 5 : return b5;
            case 6 : return b6;
            case 7 : return b7;
            case 8 : return b8;
            case 9 : return b9;
            case 10 : return b10;
            case 11 : return b11;
            case 12 : return b12;
            case 13 : return b13;
            case 14 : return b14;
            case 15 : return b15;
            case 16 : return b16;
            case 17 : return b17;
            case 18 : return b18;
            case 19 : return b19;
            case 20 : return b20;
            default: return null;
        }
    }

    private StackPane getStackC(int y) {
        switch (y) {
            case 1 : return c1;
            case 2 : return c2;
            case 3 : return c3;
            case 4 : return c4;
            case 5 : return c5;
            case 6 : return c6;
            case 7 : return c7;
            case 8 : return c8;
            case 9 : return c9;
            case 10 : return c10;
            case 11 : return c11;
            case 12 : return c12;
            case 13 : return c13;
            case 14 : return c14;
            case 15 : return c15;
            case 16 : return c16;
            case 17 : return c17;
            case 18 : return c18;
            case 19 : return c19;
            case 20 : return c20;
            default: return null;
        }
    }

    private StackPane getStackD(int y) {
        switch (y) {
            case 1 : return d1;
            case 2 : return d2;
            case 3 : return d3;
            case 4 : return d4;
            case 5 : return d5;
            case 6 : return d6;
            case 7 : return d7;
            case 8 : return d8;
            case 9 : return d9;
            case 10 : return d10;
            case 11 : return d11;
            case 12 : return d12;
            case 13 : return d13;
            case 14 : return d14;
            case 15 : return d15;
            case 16 : return d16;
            case 17 : return d17;
            case 18 : return d18;
            case 19 : return d19;
            case 20 : return d20;
            default: return null;
        }
    }

    private StackPane getStackE(int y) {
        switch (y) {
            case 1 : return e1;
            case 2 : return e2;
            case 3 : return e3;
            case 4 : return e4;
            case 5 : return e5;
            case 6 : return e6;
            case 7 : return e7;
            case 8 : return e8;
            case 9 : return e9;
            case 10 : return e10;
            case 11 : return e11;
            case 12 : return e12;
            case 13 : return e13;
            case 14 : return e14;
            case 15 : return e15;
            case 16 : return e16;
            case 17 : return e17;
            case 18 : return e18;
            case 19 : return e19;
            case 20 : return e20;
            default: return null;
        }
    }

    private StackPane getStackF(int y) {
        switch (y) {
            case 1 : return f1;
            case 2 : return f2;
            case 3 : return f3;
            case 4 : return f4;
            case 5 : return f5;
            case 6 : return f6;
            case 7 : return f7;
            case 8 : return f8;
            case 9 : return f9;
            case 10 : return f10;
            case 11 : return f11;
            case 12 : return f12;
            case 13 : return f13;
            case 14 : return f14;
            case 15 : return f15;
            case 16 : return f16;
            case 17 : return f17;
            case 18 : return f18;
            case 19 : return f19;
            case 20 : return f20;
            default: return null;
        }
    }

    private StackPane getStackG(int y) {
        switch (y) {
            case 1 : return g1;
            case 2 : return g2;
            case 3 : return g3;
            case 4 : return g4;
            case 5 : return g5;
            case 6 : return g6;
            case 7 : return g7;
            case 8 : return g8;
            case 9 : return g9;
            case 10 : return g10;
            case 11 : return g11;
            case 12 : return g12;
            case 13 : return g13;
            case 14 : return g14;
            case 15 : return g15;
            case 16 : return g16;
            case 17 : return g17;
            case 18 : return g18;
            case 19 : return g19;
            case 20 : return g20;
            default: return null;
        }
    }

    private StackPane getStackH(int y) {
        switch (y) {
            case 1 : return h1;
            case 2 : return h2;
            case 3 : return h3;
            case 4 : return h4;
            case 5 : return h5;
            case 6 : return h6;
            case 7 : return h7;
            case 8 : return h8;
            case 9 : return h9;
            case 10 : return h10;
            case 11 : return h11;
            case 12 : return h12;
            case 13 : return h13;
            case 14 : return h14;
            case 15 : return h15;
            case 16 : return h16;
            case 17 : return h17;
            case 18 : return h18;
            case 19 : return h19;
            case 20 : return h20;
            default: return null;
        }
    }

    private StackPane getStackI(int y) {
        switch (y) {
            case 1 : return i1;
            case 2 : return i2;
            case 3 : return i3;
            case 4 : return i4;
            case 5 : return i5;
            case 6 : return i6;
            case 7 : return i7;
            case 8 : return i8;
            case 9 : return i9;
            case 10 : return i10;
            case 11 : return i11;
            case 12 : return i12;
            case 13 : return i13;
            case 14 : return i14;
            case 15 : return i15;
            case 16 : return i16;
            case 17 : return i17;
            case 18 : return i18;
            case 19 : return i19;
            case 20 : return i20;
            default: return null;
        }
    }

    private StackPane getStackJ(int y) {
        switch (y) {
            case 1 : return j1;
            case 2 : return j2;
            case 3 : return j3;
            case 4 : return j4;
            case 5 : return j5;
            case 6 : return j6;
            case 7 : return j7;
            case 8 : return j8;
            case 9 : return j9;
            case 10 : return j10;
            case 11 : return j11;
            case 12 : return j12;
            case 13 : return j13;
            case 14 : return j14;
            case 15 : return j15;
            case 16 : return j16;
            case 17 : return j17;
            case 18 : return j18;
            case 19 : return j19;
            case 20 : return j20;
            default: return null;
        }
    }

    private StackPane getStackK(int y) {
        switch (y) {
            case 1 : return k1;
            case 2 : return k2;
            case 3 : return k3;
            case 4 : return k4;
            case 5 : return k5;
            case 6 : return k6;
            case 7 : return k7;
            case 8 : return k8;
            case 9 : return k9;
            case 10 : return k10;
            case 11 : return k11;
            case 12 : return k12;
            case 13 : return k13;
            case 14 : return k14;
            case 15 : return k15;
            case 16 : return k16;
            case 17 : return k17;
            case 18 : return k18;
            case 19 : return k19;
            case 20 : return k20;
            default: return null;
        }
    }

    private StackPane getStackL(int y) {
        switch (y) {
            case 1 : return l1;
            case 2 : return l2;
            case 3 : return l3;
            case 4 : return l4;
            case 5 : return l5;
            case 6 : return l6;
            case 7 : return l7;
            case 8 : return l8;
            case 9 : return l9;
            case 10 : return l10;
            case 11 : return l11;
            case 12 : return l12;
            case 13 : return l13;
            case 14 : return l14;
            case 15 : return l15;
            case 16 : return l16;
            case 17 : return l17;
            case 18 : return l18;
            case 19 : return l19;
            case 20 : return l20;
            default: return null;
        }
    }

    private StackPane getStackM(int y) {
        switch (y) {
            case 1 : return m1;
            case 2 : return m2;
            case 3 : return m3;
            case 4 : return m4;
            case 5 : return m5;
            case 6 : return m6;
            case 7 : return m7;
            case 8 : return m8;
            case 9 : return m9;
            case 10 : return m10;
            case 11 : return m11;
            case 12 : return m12;
            case 13 : return m13;
            case 14 : return m14;
            case 15 : return m15;
            case 16 : return m16;
            case 17 : return m17;
            case 18 : return m18;
            case 19 : return m19;
            case 20 : return m20;
            default: return null;
        }
    }

    private StackPane getStackN(int y) {
        switch (y) {
            case 1 : return n1;
            case 2 : return n2;
            case 3 : return n3;
            case 4 : return n4;
            case 5 : return n5;
            case 6 : return n6;
            case 7 : return n7;
            case 8 : return n8;
            case 9 : return n9;
            case 10 : return n10;
            case 11 : return n11;
            case 12 : return n12;
            case 13 : return n13;
            case 14 : return n14;
            case 15 : return n15;
            case 16 : return n16;
            case 17 : return n17;
            case 18 : return n18;
            case 19 : return n19;
            case 20 : return n20;
            default: return null;
        }
    }

    private StackPane getStackO(int y) {
        switch (y) {
            case 1 : return o1;
            case 2 : return o2;
            case 3 : return o3;
            case 4 : return o4;
            case 5 : return o5;
            case 6 : return o6;
            case 7 : return o7;
            case 8 : return o8;
            case 9 : return o9;
            case 10 : return o10;
            case 11 : return o11;
            case 12 : return o12;
            case 13 : return o13;
            case 14 : return o14;
            case 15 : return o15;
            case 16 : return o16;
            case 17 : return o17;
            case 18 : return o18;
            case 19 : return o19;
            case 20 : return o20;
            default: return null;
        }
    }

    private StackPane getStackP(int y) {
        switch (y) {
            case 1 : return p1;
            case 2 : return p2;
            case 3 : return p3;
            case 4 : return p4;
            case 5 : return p5;
            case 6 : return p6;
            case 7 : return p7;
            case 8 : return p8;
            case 9 : return p9;
            case 10 : return p10;
            case 11 : return p11;
            case 12 : return p12;
            case 13 : return p13;
            case 14 : return p14;
            case 15 : return p15;
            case 16 : return p16;
            case 17 : return p17;
            case 18 : return p18;
            case 19 : return p19;
            case 20 : return p20;
            default: return null;
        }
    }

    private StackPane getStackQ(int y) {
        switch (y) {
            case 1 : return q1;
            case 2 : return q2;
            case 3 : return q3;
            case 4 : return q4;
            case 5 : return q5;
            case 6 : return q6;
            case 7 : return q7;
            case 8 : return q8;
            case 9 : return q9;
            case 10 : return q10;
            case 11 : return q11;
            case 12 : return q12;
            case 13 : return q13;
            case 14 : return q14;
            case 15 : return q15;
            case 16 : return q16;
            case 17 : return q17;
            case 18 : return q18;
            case 19 : return q19;
            case 20 : return q20;
            default: return null;
        }
    }

    private StackPane getStackR(int y) {
        switch (y) {
            case 1 : return r1;
            case 2 : return r2;
            case 3 : return r3;
            case 4 : return r4;
            case 5 : return r5;
            case 6 : return r6;
            case 7 : return r7;
            case 8 : return r8;
            case 9 : return r9;
            case 10 : return r10;
            case 11 : return r11;
            case 12 : return r12;
            case 13 : return r13;
            case 14 : return r14;
            case 15 : return r15;
            case 16 : return r16;
            case 17 : return r17;
            case 18 : return r18;
            case 19 : return r19;
            case 20 : return r20;
            default: return null;
        }
    }

    private StackPane getStackS(int y) {
        switch (y) {
            case 1 : return s1;
            case 2 : return s2;
            case 3 : return s3;
            case 4 : return s4;
            case 5 : return s5;
            case 6 : return s6;
            case 7 : return s7;
            case 8 : return s8;
            case 9 : return s9;
            case 10 : return s10;
            case 11 : return s11;
            case 12 : return s12;
            case 13 : return s13;
            case 14 : return s14;
            case 15 : return s15;
            case 16 : return s16;
            case 17 : return s17;
            case 18 : return s18;
            case 19 : return s19;
            case 20 : return s20;
            default: return null;
        }
    }

    private StackPane getStackT(int y) {
        switch (y) {
            case 1 : return t1;
            case 2 : return t2;
            case 3 : return t3;
            case 4 : return t4;
            case 5 : return t5;
            case 6 : return t6;
            case 7 : return t7;
            case 8 : return t8;
            case 9 : return t9;
            case 10 : return t10;
            case 11 : return t11;
            case 12 : return t12;
            case 13 : return t13;
            case 14 : return t14;
            case 15 : return t15;
            case 16 : return t16;
            case 17 : return t17;
            case 18 : return t18;
            case 19 : return t19;
            case 20 : return t20;
            default: return null;
        }
    }

    //endregion
}
