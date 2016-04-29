package de.tassenmann.ibeacons_poc;

import java.util.UUID;

import com.estimote.sdk.Region;

public class IBeacons
{
    public static final Region BLUEBERRY_REGION = new Region("Blueberry iBeacon", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 5745, 49316);
    public static final Region MINT_REGION = new Region("Mint iBeacon", UUID.fromString("b9407f30-f5f8-466e-aff9-25556b57fe6d"), 14350, 61923);
    public static final Region ICE_REGION = new Region("Ice iBeacon", UUID.fromString("b9407f30-f5f8-466e-aff9-25556b57fe6d"), 3148, 51628);

    private IBeacons()
    {
    }
}
