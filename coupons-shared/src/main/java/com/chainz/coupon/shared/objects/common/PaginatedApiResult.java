package com.chainz.coupon.shared.objects.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Paginated api result.
 *
 * @param <T> entity class.
 */
@Getter
@Setter
public class PaginatedApiResult<T> {

  private Integer page;

  private Integer count;

  private Long total;

  private List<T> data;

  /**
   * Constructor without total.
   *
   * @param page page
   * @param count count
   * @param data content
   */
  public PaginatedApiResult(Integer page, Integer count, List<T> data) {
    this(page, count, null, data);
  }

  /**
   * Constructor with total.
   *
   * @param page page.
   * @param count count.
   * @param total total.
   * @param data data.
   */
  public PaginatedApiResult(Integer page, Integer count, Long total, List<T> data) {
    this.page = page;
    this.count = count;
    this.total = total;
    this.data = data;
  }
}
