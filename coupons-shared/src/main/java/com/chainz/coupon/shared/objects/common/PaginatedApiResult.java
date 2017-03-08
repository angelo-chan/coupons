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

  private Integer size;

  private Integer count;

  private Long total;

  private List<T> data;

  /**
   * Constructor without total.
   *
   * @param page page index
   * @param size page size
   * @param count count
   * @param data content
   */
  public PaginatedApiResult(Integer page, Integer size, Integer count, List<T> data) {
    this(page, size, count, null, data);
  }

  /**
   * Constructor with total.
   *
   * @param page page index.
   * @param size page size.
   * @param count count in this page.
   * @param total total count.
   * @param data data.
   */
  public PaginatedApiResult(Integer page, Integer size, Integer count, Long total, List<T> data) {
    this.page = page;
    this.size = size;
    this.count = count;
    this.total = total;
    this.data = data;
  }
}
