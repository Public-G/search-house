package com.github.common.validator.group;

import javax.validation.GroupSequence;

/**
 * 定义校验顺序，如果AddGroup组失败，则UpdateGroup组不会再校验
 *
 * @author ZEALER
 * @date 2018-10-23
 */
@GroupSequence({AddGroup.class, UpdateGroup.class})
public interface Group {
}
