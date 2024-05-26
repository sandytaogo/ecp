package com.sandy.ecp.framework.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

@Component
public class BundleMessageSource extends ReloadableResourceBundleMessageSource {
	
	private String locationPattern;
	private String location;

	public BundleMessageSource() {
		this.locationPattern = "classpath*:/META-INF/ecp-i18n/*.properties";
	}

	public String getLocationPattern() {
		return this.locationPattern;
	}

	public void setLocationPattern(final String locationPattern) {
		this.locationPattern = locationPattern;
		this.location = locationPattern.replaceAll("\\*", "");
		this.location = this.location.substring(0, this.location.lastIndexOf("/") + 1);
	}

	@PostConstruct
	public void init() throws Exception {
		final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		final Resource[] resources = resolver.getResources(this.locationPattern);
		final Map<String, String> fileNames = new HashMap<String, String>();
		for (final Resource resource : resources) {
			final String url = resource.toString();
			final String fileName = url.substring(url.lastIndexOf("/") + 1);
			if (logger.isErrorEnabled() && fileNames.containsKey(fileName)) {
				this.logger.error((Object) ("Resource file [" + url + "] dup with [" + fileNames.get(fileName)));
			}
			fileNames.put(fileName, url);
		}
		final Set<String> fNames = fileNames.keySet();
		final List<String> baseNames = new ArrayList<String>();
		for (final String fname : fNames) {
			String baseName = fname.substring(0, fname.lastIndexOf("."));
			if (baseName.indexOf("_") > 0) {
				baseName = baseName.substring(0, baseName.indexOf("_"));
			}
			baseName = this.location + baseName;
			if (!baseNames.contains(baseName)) {
				baseNames.add(baseName);
			}
		}
		if (this.logger.isInfoEnabled()) {
			this.logger.info((Object) ("All base names parsed from " + this.locationPattern + " is "
					+ baseNames.toString() + "."));
		}
		super.setBasenames((String[]) baseNames.toArray(new String[0]));
	}
}
