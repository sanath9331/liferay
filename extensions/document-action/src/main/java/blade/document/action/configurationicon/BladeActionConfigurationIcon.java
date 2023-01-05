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

package blade.document.action.configurationicon;

import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.configuration.icon.BasePortletConfigurationIcon;
import com.liferay.portal.kernel.portlet.configuration.icon.PortletConfigurationIcon;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.WindowStateException;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Adds the new context menu option to the Document Detail screen options (top
 * right corner) of the Documents and Media Admin portlet.
 *
 * @author liferay
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=com_liferay_document_library_web_portlet_DLAdminPortlet",
		"path=/document_library/view_file_entry"
	},
	service = PortletConfigurationIcon.class
)
public class BladeActionConfigurationIcon extends BasePortletConfigurationIcon {

	public String getMessage(PortletRequest portletRequest) {
		return "Blade Basic Info";
	}

	public String getURL(
		PortletRequest portletRequest, PortletResponse portletResponse) {

		HttpServletRequest httpServletRequest = _portal.getHttpServletRequest(
			portletRequest);

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		FileEntry fileEntry = _retrieveFile(httpServletRequest);

		PortletURL portletURL = PortletURLFactoryUtil.create(
			httpServletRequest,
			"blade_document_action_portlet_BladeDocumentActionPortlet",
			themeDisplay.getPlid(), PortletRequest.RENDER_PHASE);

		String fileName = fileEntry.getFileName();
		String mimeType = fileEntry.getMimeType();
		String version = fileEntry.getVersion();
		String createdDate = String.valueOf(fileEntry.getCreateDate());
		String createdUserName = fileEntry.getUserName();

		String statusLabel = null;

		try {
			FileVersion fileVersion = fileEntry.getLatestFileVersion();

			statusLabel = WorkflowConstants.getStatusLabel(
				fileVersion.getStatus());
		}
		catch (PortalException portalException) {
			_log.error(portalException.getMessage(), portalException);
		}

		portletURL.setParameter("fileName", fileName);
		portletURL.setParameter("mimeType", mimeType);
		portletURL.setParameter("version", version);
		portletURL.setParameter("statusLabel", statusLabel);
		portletURL.setParameter("createdDate", createdDate);
		portletURL.setParameter("createdUserName", createdUserName);

		try {
			portletURL.setWindowState(LiferayWindowState.POP_UP);
		}
		catch (WindowStateException windowStateException) {
			_log.error(windowStateException.getMessage(), windowStateException);
		}

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("javascript:Liferay.Util.openWindow(");
		stringBuilder.append("{dialog: {cache: false,width:800,modal: true},");
		stringBuilder.append("title: 'basic information',id: ");
		stringBuilder.append("'testPopupIdUnique',uri: '");
		stringBuilder.append(portletURL.toString() + "'});");

		return stringBuilder.toString();
	}

	public boolean isShow(PortletRequest portletRequest) {
		return true;
	}

	private FileEntry _retrieveFile(HttpServletRequest httpServletRequest) {
		try {
			long fileEntryId = ParamUtil.getLong(
				httpServletRequest, "fileEntryId");

			FileEntry fileEntry = null;

			if (fileEntryId > 0) {
				fileEntry = _dlAppService.getFileEntry(fileEntryId);
			}

			if (fileEntry == null) {
				return null;
			}

			String cmd = ParamUtil.getString(httpServletRequest, Constants.CMD);

			if (fileEntry.isInTrash() &&
				!cmd.equals(Constants.MOVE_FROM_TRASH)) {

				return null;
			}

			return fileEntry;
		}
		catch (PortalException portalException) {
			_log.error(portalException.getMessage(), portalException);

			return null;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BladeActionConfigurationIcon.class);

	@Reference
	private DLAppService _dlAppService;

	@Reference
	private Portal _portal;

}