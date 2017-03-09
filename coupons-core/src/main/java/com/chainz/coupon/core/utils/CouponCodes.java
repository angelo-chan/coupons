package com.chainz.coupon.core.utils;

import java.util.Random;

/** Utility class to resolve coupon code. */
public class CouponCodes {

  public static final int DEFAULT_LENGTH = 16;

  public interface Charset {
    String ALPHABETIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String ALPHANUMERIC = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String NUMBERS = "0123456789";
  }

  /**
   * Generate coupon code by default.
   *
   * @return default coupon code.
   */
  public static String generateDefault() {
    return generate(Charset.NUMBERS, 16);
  }

  /**
   * Generate a random coupon code.
   *
   * @param charset charset
   * @param length pattern length
   * @return  random coupon code.
   */
  public static String generate(String charset, int length) {
    Random random = new Random(System.currentTimeMillis());
    StringBuilder sb = new StringBuilder();
    char[] chars = charset.toCharArray();
    for (int i = 0; i < length; i++) {
      sb.append(chars[random.nextInt(chars.length)]);
    }
    return sb.toString();
  }
}
