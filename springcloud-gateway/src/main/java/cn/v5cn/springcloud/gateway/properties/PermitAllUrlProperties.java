package cn.v5cn.springcloud.gateway.properties;

import com.google.common.collect.Lists;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

@Data
public class PermitAllUrlProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(PermitAllUrlProperties.class);

    private static List<Pattern> permitallUrlPattern;

    private List<url> permitall;

    public String[] getPermitallUrlPatterns(){
        List<String> urls = Lists.newArrayList();
        Iterator<url> iterator = permitall.iterator();
        while(iterator.hasNext()) {
            urls.add(iterator.next().getPattern());
        }
        return urls.toArray(new String[0]);
    }

    public static class url {
        private String pattern;

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }
    }

    @PostConstruct
    public void init(){
        if(!CollectionUtils.isEmpty(permitall)) {
            permitallUrlPattern = Lists.newArrayList();
            Iterator<url> iterator = permitall.iterator();
            while(iterator.hasNext()) {
                String currentUrl = iterator.next().getPattern().replaceAll("\\*\\*", "(.*?)");
                LOGGER.info(currentUrl);
                Pattern currentPattern = Pattern.compile(currentUrl, Pattern.CASE_INSENSITIVE);
                permitallUrlPattern.add(currentPattern);
            }
        }
    }

    public boolean isPermitAllUrl(String url) {
        for(Pattern pattern : permitallUrlPattern) {
            if(pattern.matcher(url).find()) {
                return true;
            }
        }
        return false;
    }
}
