package org.open.util;


import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

public class JsonUtilTest {

	@Test
	public void testToMap() {
		String json = "{\"count\":20,\"start\":0,\"total\":13,\"subjects\":[{\"rating\":{\"max\":10,\"average\":6.9,\"stars\":\"35\",\"min\":0},\"title\":\"十二生肖\",\"original_title\":\"十二生肖\",\"subtype\":\"movie\",\"year\":\"2012\",\"images\":{\"small\":\"http://img3.douban.com/spic/s24519706.jpg\",\"large\":\"http://img3.douban.com/lpic/s24519706.jpg\",\"medium\":\"http://img3.douban.com/mpic/s24519706.jpg\"},\"alt\":\"http://movie.douban.com/subject/4212172/\",\"id\":\"4212172\"},{\"rating\":{\"max\":10,\"average\":8.4,\"stars\":\"45\",\"min\":0},\"title\":\"十二生肖之死\",\"original_title\":\"十二生肖\",\"subtype\":\"tv\",\"year\":\"1995\",\"images\":{\"small\":\"http://img3.douban.com/spic/s24526014.jpg\",\"large\":\"http://img3.douban.com/lpic/s24526014.jpg\",\"medium\":\"http://img3.douban.com/mpic/s24526014.jpg\"},\"alt\":\"http://movie.douban.com/subject/2932856/\",\"id\":\"2932856\"},{\"rating\":{\"max\":10,\"average\":5.3,\"stars\":\"30\",\"min\":0},\"title\":\"生肖传奇之十二生肖总动员\",\"original_title\":\"生肖传奇之十二生肖总动员\",\"subtype\":\"tv\",\"year\":\"\",\"images\":{\"small\":\"http://img3.douban.com/spic/s4446854.jpg\",\"large\":\"http://img3.douban.com/lpic/s4446854.jpg\",\"medium\":\"http://img3.douban.com/mpic/s4446854.jpg\"},\"alt\":\"http://movie.douban.com/subject/5041060/\",\"id\":\"5041060\"},{\"rating\":{\"max\":10,\"average\":0,\"stars\":\"00\",\"min\":0},\"title\":\"十二生肖之五福外传\",\"original_title\":\"十二生肖之五福外传\",\"subtype\":\"tv\",\"year\":\"2011\",\"images\":{\"small\":\"http://img4.douban.com/spic/s4632498.jpg\",\"large\":\"http://img4.douban.com/lpic/s4632498.jpg\",\"medium\":\"http://img4.douban.com/mpic/s4632498.jpg\"},\"alt\":\"http://movie.douban.com/subject/5975116/\",\"id\":\"5975116\"},{\"rating\":{\"max\":10,\"average\":0,\"stars\":\"00\",\"min\":0},\"title\":\"生肖传奇之十二生肖闯江湖\",\"original_title\":\"生肖传奇之十二生肖闯江湖\",\"subtype\":\"tv\",\"year\":\"2012\",\"images\":{\"small\":\"http://img3.douban.com/view/photo/icon/public/p1917631116.jpg\",\"large\":\"http://img3.douban.com/view/movie_poster_cover/lpst/public/p1917631116.jpg\",\"medium\":\"http://img3.douban.com/view/movie_poster_cover/spst/public/p1917631116.jpg\"},\"alt\":\"http://movie.douban.com/subject/21348822/\",\"id\":\"21348822\"},{\"rating\":{\"max\":10,\"average\":3.9,\"stars\":\"20\",\"min\":0},\"title\":\"十二生肖传奇\",\"original_title\":\"十二生肖传奇\",\"subtype\":\"tv\",\"year\":\"2011\",\"images\":{\"small\":\"http://img3.douban.com/spic/s6978297.jpg\",\"large\":\"http://img3.douban.com/lpic/s6978297.jpg\",\"medium\":\"http://img3.douban.com/mpic/s6978297.jpg\"},\"alt\":\"http://movie.douban.com/subject/4920537/\",\"id\":\"4920537\"},{\"rating\":{\"max\":10,\"average\":7.5,\"stars\":\"40\",\"min\":0},\"title\":\"新十二生肖\",\"original_title\":\"新十二生肖\",\"subtype\":\"movie\",\"year\":\"1991\",\"images\":{\"small\":\"http://img4.douban.com/spic/s2407108.jpg\",\"large\":\"http://img4.douban.com/lpic/s2407108.jpg\",\"medium\":\"http://img4.douban.com/mpic/s2407108.jpg\"},\"alt\":\"http://movie.douban.com/subject/2081545/\",\"id\":\"2081545\"},{\"rating\":{\"max\":10,\"average\":0,\"stars\":\"00\",\"min\":0},\"title\":\"十二生肖全家福\",\"original_title\":\"十二生肖全家福\",\"subtype\":\"tv\",\"year\":\"1993\",\"images\":{\"small\":\"http://img3.douban.com/pics/movie-default-small.gif\",\"large\":\"http://img3.douban.com/pics/movie-default-large.gif\",\"medium\":\"http://img3.douban.com/pics/movie-default-medium.gif\"},\"alt\":\"http://movie.douban.com/subject/23752516/\",\"id\":\"23752516\"},{\"rating\":{\"max\":10,\"average\":8.5,\"stars\":\"45\",\"min\":0},\"title\":\"十二生肖守护神\",\"original_title\":\"十二戦支 爆裂エトレンジャー\",\"subtype\":\"tv\",\"year\":\"1995\",\"images\":{\"small\":\"http://img3.douban.com/spic/s2753442.jpg\",\"large\":\"http://img3.douban.com/lpic/s2753442.jpg\",\"medium\":\"http://img3.douban.com/mpic/s2753442.jpg\"},\"alt\":\"http://movie.douban.com/subject/2295831/\",\"id\":\"2295831\"},{\"rating\":{\"max\":10,\"average\":0,\"stars\":\"00\",\"min\":0},\"title\":\"十二生肖奇缘\",\"original_title\":\"十二生肖奇缘\",\"subtype\":\"movie\",\"year\":\"1926\",\"images\":{\"small\":\"http://img4.douban.com/spic/s3547928.jpg\",\"large\":\"http://img4.douban.com/lpic/s3547928.jpg\",\"medium\":\"http://img4.douban.com/mpic/s3547928.jpg\"},\"alt\":\"http://movie.douban.com/subject/3422958/\",\"id\":\"3422958\"},{\"rating\":{\"max\":10,\"average\":0,\"stars\":\"00\",\"min\":0},\"title\":\"十二生肖\",\"original_title\":\"飞鹰计划3\",\"subtype\":\"movie\",\"year\":\"2012\",\"images\":{\"small\":\"http://img3.douban.com/pics/movie-default-small.gif\",\"large\":\"http://img3.douban.com/pics/movie-default-large.gif\",\"medium\":\"http://img3.douban.com/pics/movie-default-medium.gif\"},\"alt\":\"http://movie.douban.com/subject/2279584/\",\"id\":\"2279584\"},{\"rating\":{\"max\":10,\"average\":0,\"stars\":\"00\",\"min\":0},\"title\":\"12生肖全家福的神奇世界\",\"original_title\":\"12生肖全家福的神奇世界\",\"subtype\":\"tv\",\"year\":\"1999\",\"images\":{\"small\":\"http://img3.douban.com/view/photo/icon/public/p1930501182.jpg\",\"large\":\"http://img3.douban.com/view/photo/photo/public/p1930501182.jpg\",\"medium\":\"http://img3.douban.com/view/photo/thumb/public/p1930501182.jpg\"},\"alt\":\"http://movie.douban.com/subject/23752329/\",\"id\":\"23752329\"},{\"rating\":{\"max\":10,\"average\":0,\"stars\":\"00\",\"min\":0},\"title\":\"第十二張生肖卡\",\"original_title\":\"第十二張生肖卡\",\"subtype\":\"movie\",\"year\":\"2012\",\"images\":{\"small\":\"http://img3.douban.com/spic/s10277287.jpg\",\"large\":\"http://img3.douban.com/lpic/s10277287.jpg\",\"medium\":\"http://img3.douban.com/mpic/s10277287.jpg\"},\"alt\":\"http://movie.douban.com/subject/10777774/\",\"id\":\"10777774\"}],\"title\":\"搜索 十二生肖 的结果\"}";
		Map<String, Object> map = JsonUtil.toMap(json);
		Iterator<String> keys = map.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			Object value = map.get(key);
			System.out.println(key + ":");
			System.out.println(value.getClass() + "->" + value);
		}
		System.out.println(map.size());
	}
}