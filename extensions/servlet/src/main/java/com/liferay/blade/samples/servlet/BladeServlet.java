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

package com.liferay.blade.samples.servlet;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.util.ContentTypes;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Liferay
 */
@Component(
	immediate = true,
	property = {
		"osgi.http.whiteboard.context.path=/",
		"osgi.http.whiteboard.servlet.pattern=/blade/servlet/*"
	},
	service = Servlet.class
)
public class BladeServlet extends HttpServlet {

	@Override
	public void init() throws ServletException {
		if (_log.isInfoEnabled()) {
			_log.info("BladeServlet init");
		}

		super.init();
	}

	@Override
	protected void doGet(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException, ServletException {

		if (_log.isInfoEnabled()) {
			_log.info("doGet");
		}

		_writeSampleHTML(httpServletResponse);
	}

	/**
	 * Dummy contents
	 *
	 * @return dummy contents string
	 */
	private String _generateSampleHTML() {
		StringBuffer sb = new StringBuffer();

		sb.append("<html>");
		sb.append("<head><title>Sample HTML</title></head>");
		sb.append("<body>");
		sb.append("<h2>Hello World</h2>");
		sb.append("</body>");
		sb.append("</html>");

		return new String(sb);
	}

	/**
	 * Write sample HTML
	 *
	 * @param httpServletResponse
	 */
	private void _writeSampleHTML(HttpServletResponse httpServletResponse) {
		httpServletResponse.setCharacterEncoding(StringPool.UTF8);
		httpServletResponse.setContentType(ContentTypes.TEXT_HTML_UTF8);
		httpServletResponse.setStatus(HttpServletResponse.SC_OK);

		try {
			ServletResponseUtil.write(
				httpServletResponse, _generateSampleHTML());
		}
		catch (Exception exception) {
			_log.warn(exception.getMessage(), exception);

			httpServletResponse.setStatus(
				HttpServletResponse.SC_PRECONDITION_FAILED);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(BladeServlet.class);

	private static final long serialVersionUID = 1L;

}