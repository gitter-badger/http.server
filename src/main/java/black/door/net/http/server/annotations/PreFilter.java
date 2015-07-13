package black.door.net.http.server.annotations;

import black.door.net.http.server.Filter;

import java.lang.annotation.*;

/**
 * Created by nfischer on 7/9/2015.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Repeatable(PreFilters.class)
public @interface PreFilter {
	Class<? extends Filter> value();
}
