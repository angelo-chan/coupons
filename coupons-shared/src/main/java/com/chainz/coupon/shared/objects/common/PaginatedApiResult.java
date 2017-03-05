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

  private List<T> data;

  private Integer page;

  private Integer size;

  private Integer total;

  /** No args constructor. */
  public PaginatedApiResult() {}

  /**
   * Constructor without total.
   *
   * @param data content
   * @param page page
   * @param size size
   */
  public PaginatedApiResult(List<T> data, Integer page, Integer size) {
    this(data, page, size, null);
  }

  /**
   * Constructor with total.
   *
   * @param data data.
   * @param page page.
   * @param size size.
   * @param total total.
   */
  public PaginatedApiResult(List<T> data, Integer page, Integer size, Integer total) {
    this.data = data;
    this.page = page;
    this.size = size;
    this.total = total;
  }
}
