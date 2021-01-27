/*
 * Copyright 2020 the original author or authors.
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

package org.springframework.cloud.dataflow.outbox;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Sample configuration properties class.
 * Modify this class as needed to fit the custom use cases.
 */
@ConfigurationProperties("shipment")
public class ShipmentProperties {

	/**
	 * Flag to control which case to use.
	 */
	private boolean uppercase = true;

	public boolean isUppercase() {
		return uppercase;
	}

	public void setUppercase(boolean uppercase) {
		this.uppercase = uppercase;
	}
}
