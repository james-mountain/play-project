package utils.filter

import javax.inject.Inject

import play.api.http.DefaultHttpFilters

/**
  * Created by Administrator on 02/08/2017.
  */
class Filters @Inject() (
  loggingFilter: LoggingFilter
) extends DefaultHttpFilters(loggingFilter) {}
