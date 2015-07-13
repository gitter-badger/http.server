package black.door.net.http.server.annotations;

import black.door.net.http.server.Filter;

import java.lang.annotation.*;

/**
 * Created by nfischer on 7/10/2015.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Repeatable(PostFilters.class)
public @interface PostFilter{
	Class<? extends Filter> value();
}
