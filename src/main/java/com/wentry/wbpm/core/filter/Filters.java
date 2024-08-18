package com.wentry.wbpm.core.filter;

import com.google.common.collect.Lists;


import java.util.List;

/**
 * @Description:
 * @Author: tangwc
 */

public class Filters {

    /**
     * 这里可以在扩展一下，根据类别的不同，加载不同的filter，目前是写死的
     */
    public static final List<NodeFilter> defaultFilters = Lists.newArrayList(
            ActiveAwareFilter.instance,
            LogFilter.instance
    );

}
