/**
 * Copyright 2000-present Liferay, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liferay.blade.samples.dspservicebuilder.service.impl;

import com.liferay.blade.samples.dspservicebuilder.service.base.CountryLocalServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Component;

/**
 * The implementation of the country local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.blade.samples.dspservicebuilder.service.CountryLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see CountryLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=com.liferay.blade.samples.dspservicebuilder.model.Country",
	service = AopService.class
)
public class CountryLocalServiceImpl extends CountryLocalServiceBaseImpl {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Use <code>com.liferay.blade.samples.dspservicebuilder.service.CountryLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.blade.samples.dspservicebuilder.service.CountryLocalServiceUtil</code>.
	 */
	public void useDSP() {
		try {
			DataSource dataSource = countryPersistence.getDataSource();

			Connection connection = dataSource.getConnection();

			Statement statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(
				"select id, name from country");

			while (resultSet.next()) {
				if (_log.isInfoEnabled()) {
					_log.info("Record from external database:");
				}

				if (_log.isInfoEnabled()) {
					String id = resultSet.getString("id");

					_log.info("ID: " + id);
				}

				if (_log.isInfoEnabled()) {
					String name = resultSet.getString("name");

					_log.info("Name: " + name + System.lineSeparator());
				}
			}

			connection.close();
		}
		catch (SQLException sqlException) {
			_log.error(
				"Failed to retrieve data from external database!",
				sqlException);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CountryLocalServiceImpl.class);

}