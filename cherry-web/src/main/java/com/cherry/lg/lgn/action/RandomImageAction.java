/*  
 * @(#)RandomImageAction.java     1.0 2011/05/31      
 *      
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD       
 * All rights reserved      
 *      
 * This software is the confidential and proprietary information of         
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not        
 * disclose such Confidential Information and shall use it only in      
 * accordance with the terms of the license agreement you entered into      
 * with SHANGHAI BINGKUN.       
 */
package com.cherry.lg.lgn.action;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.engine.image.gimpy.DefaultGimpyEngine;
import com.octo.captcha.service.captchastore.MapCaptchaStore;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;

public class RandomImageAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2357125105592458251L;
	private ByteArrayInputStream inputStream;
	//private static DefaultManageableImageCaptchaService instance;

	public String getRandomCode() throws Exception {
		try {

			// get the session id that will identify the generated captcha.
			// the same id must be used to validate the response, the session id
			// is a good candidate!
			String captchaId = request.getSession().getId();
			// call the ImageCaptchaService getChallenge method
			BufferedImage challenge; // = new BufferedImage(200, 50, 1);

			final MapCaptchaStore store = new MapCaptchaStore();
			final CaptchaEngine engine = new DefaultGimpyEngine();
			DefaultManageableImageCaptchaService obj = new DefaultManageableImageCaptchaService(
					store, engine, 180, 100000, 75000);
			challenge = obj.getImageChallengeForID(captchaId, request.getLocale());
			BufferedImage clone = new BufferedImage(challenge.getWidth(),challenge.getHeight(), challenge.getType());
			
			clone.getGraphics().drawImage(challenge, 0, 0, clone.getWidth(),clone.getHeight(), null);			
			clone.getGraphics().dispose();
			session.put(CherryConstants.SESSION_CHECK_IMAGE, obj);

			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImageOutputStream imageOut = ImageIO
					.createImageOutputStream(output);
			ImageIO.write(clone, "JPEG", imageOut);
			imageOut.close();
			ByteArrayInputStream input = new ByteArrayInputStream(output
					.toByteArray());
			this.setInputStream(input);

			return SUCCESS;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void setInputStream(ByteArrayInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public ByteArrayInputStream getInputStream() {
		return inputStream;
	}
}
