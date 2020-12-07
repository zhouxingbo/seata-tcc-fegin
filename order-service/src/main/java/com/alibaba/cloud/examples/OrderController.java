/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.cloud.examples;

import com.alibaba.cloud.examples.feign.AccountClient;
import com.alibaba.cloud.examples.feign.StorageClient;
import io.seata.core.context.RootContext;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaojing
 */
@RestController
public class OrderController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderTccAction orderTccAction;

	@Autowired
	private AccountClient accountClient;

	@Autowired
	private StorageClient storageClient;

	@PostMapping(value = "/order", produces = "application/json")
	public void order(String userId, String commodityCode, int orderCount) {
		LOGGER.info("Order Service Begin ... xid: " + RootContext.getXID());

		// 从全局唯一id发号器获得id
		Order order = new Order();
		Long orderId = System.currentTimeMillis();
		order.setId(orderId);

		// orderMapper.create(order);

		// 这里修改成调用 TCC 第一节端方法
		orderTccAction.prepareCreateOrder(
				null,
				order.getId(),
				order.getUserId(),
				order.getProductId(),
				order.getCount(),
				order.getMoney());


		// 修改库存
		storageClient.decrease(order.getProductId(), order.getCount());

		// 修改账户余额
		accountClient.decrease(order.getUserId(), order.getMoney());

	}


}
