package com.tass.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlaneTypeEnum {
    A124(6),
    A20N(195),
    A21N(240),
    A306(266),
    A310(250),
    A318(117),
    A319(134),
    A320(164),
    A321(199),
    A332(293),
    A333(335),
    A343(335),
    A346(419),
    A359(325),
    A35K(387),
    A388(853),
    ASTR(11),
    AT76(70),
    B38M(210),
    B39M(220),
    B722(189),
    B733(149),
    B734(168),
    B735(132),
    B736(130),
    B737(148),
    B738(189),
    B739(215),
    B744(299),
    B748(467),
    B752(234),
    B753(289),
    B762(255),
    B763(350),
    B764(375),
    B772(400),
    B773(451),
    B77L(400),
    B77W(451),
    B788(381),
    B789(420),
    B78X(420),
    BCS1(133),
    BCS3(160),
    C55B(8),
    CL60(19),
    CRJ7(86),
    CRJ9(90),
    CRJX(100),
    DC10(380),
    DC87(177),
    DH8D(78),
    E135(37),
    E145(50),
    E170(80),
    E190(114),
    E195(124),
    E290(106),
    E45X(50),
    E55P(6),
    E75L(88),
    E75S(88),
    FA20(14),
    GLF4(19),
    HDJT(6),
    LJ35(8),
    LJ45(9),
    MD11(410),
    MD83(172),
    PC24(10),
    SU95(103);

    private int numberOfPassengers;
}
