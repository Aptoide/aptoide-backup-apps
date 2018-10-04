package pt.aptoide.backupapps;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 02-08-2013
 * Time: 15:59
 * To change this template use File | Settings | File Templates.
 */
public class BackedUpRefreshEvent {

  private final EnumSortBy sort;

  public BackedUpRefreshEvent(EnumSortBy sort) {

    this.sort = sort;
  }

  public EnumSortBy getSort() {
    return sort;
  }
}
