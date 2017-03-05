package com.chainz.coupon.core.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/** Coupon extension. */
@Data
@Embeddable
public class CouponExtension implements Serializable {

  private static final long serialVersionUID = 8883095183177397606L;

  @Column(name = "custom_link_name", length = 1024)
  private String customLinkName;

  @Column(name = "custom_link_title", length = 1024)
  private String customLinkTitle;

  @Column(name = "custom_link_url", length = 1024)
  private String customLinkUrl;
}
