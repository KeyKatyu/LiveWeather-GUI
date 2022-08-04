package fr.keykatyu.liveweathergui.util;

public enum RealHoursToMCHours {

    H_0(18000), H_1(19000), H_2(20000), H_3(21000),H_4(22000), H_5(23000),
    H_6(24000), H_7(1000), H_8(2000), H_9(3000),H_10(4000), H_11(5000),
    H_12(6000), H_13(7000), H_14(8000), H_15(9000),H_16(10000), H_17(11000),
    H_18(12000), H_19(13000), H_20(14000), H_21(15000),H_22(16000), H_23(17000);

    private int time;

    RealHoursToMCHours(int i) {
        this.time = i;
    }

    public int getTime() {
        return time;
    }
}
