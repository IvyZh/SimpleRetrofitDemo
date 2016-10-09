# SimpleRetrofitDemo
简单的Retrofit示例

* Retrofit是Square公司开发的一款针对Android网络请求的框架，Retrofit2底层基于OkHttp实现的，OkHttp现在已经得到Google官方认可，大量的app都采用OkHttp做网络请求

* 本案例使用的是 `Retrofit2` + `OkHttp3` + `Glide`


![](https://github.com/IvyZh/SimpleRetrofitDemo/blob/master/cover.png)

* [豆瓣API](https://developers.douban.com/wiki/?title=movie_v2#subject)

* 电影条目信息 https://api.douban.com
	* https://api.douban.com/v2/movie/subject/1764796

* TOP250
	* /v2/movie/top250



### 步骤
* 依赖的引入

		compile 'com.orhanobut:logger:1.3'  // Logger
	
	    compile 'com.squareup.retrofit2:retrofit:2.0.0' // retrofit2
	    compile 'com.squareup.retrofit2:converter-gson:2.0.0' //gson转换
	    compile 'com.squareup.retrofit2:adapter-rxjava:2.0.0' // 暂时没用到
	    compile 'com.squareup.okhttp3:okhttp:3.0.1' // okhttp3
	    compile 'com.squareup.okhttp3:logging-interceptor:3.0.1' // okhttp的拦截器
	
	    compile 'com.github.bumptech.glide:glide:3.7.0'// glide
	
	    compile 'com.github.bumptech.glide:okhttp3-integration:1.4.0@aar' //glide 集成okhttp3


* Retrofit2Utils

		public abstract class Retrofit2Utils {
		    private static Retrofit mRetrofit = null;
		    private static String BASE_URL = "https://api.douban.com";
		    private static OkHttpClient mOkHttpClient;
		
		    public static Retrofit getRetrofit() {
		
		        if (mRetrofit == null) {
		            synchronized (Retrofit2Utils.class) {
		                if (mOkHttpClient == null) {
		                    mOkHttpClient = OkHttp3Utils.getOkHttpClient();
		                }
		                if (mRetrofit == null) {
		                    mRetrofit = new Retrofit.Builder()
		                            .baseUrl(BASE_URL)
		                            .addConverterFactory(GsonConverterFactory.create())
		                            .client(mOkHttpClient)
		                            .build();
		                }
		            }
		        }
		
		        return mRetrofit;
		
		    }
		}


* OkHttp3Utils


		public class OkHttp3Utils {
		    private static OkHttpClient mOkHttpClient = null;
		    private static int cacheSize = 10 << 20; // 10 MiB
		    private static Cache cache = new Cache(MyApplication.getContext().getCacheDir(), cacheSize);
		
		    private OkHttp3Utils() {
		
		    }
		
		    public static OkHttpClient getOkHttpClient() {
		        if (mOkHttpClient == null) {
		            synchronized (OkHttpClient.class) {
		                if (mOkHttpClient == null) {
		//                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		//                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		
		
		                    mOkHttpClient = new OkHttpClient.Builder()
		                            .cookieJar(new CookiesManager())//设置一个自动管理cookies的管理器
		                            .cache(cache)
		//                            .addInterceptor(new MyIntercepter())//添加拦截器
		                            //添加网络连接器
		//                            .addNetworkInterceptor(new CookiesInterceptor(MyApplication.getContext()))//让所有网络请求都附上你的拦截器，我这里设置了一个 token 拦截器，就是在所有网络请求的 header 加上 token 参数
		                            .retryOnConnectionFailure(true)//方法为设置出现错误进行重新连接。
		                            .connectTimeout(15, TimeUnit.SECONDS)//设置超时时间
		                            .readTimeout(20, TimeUnit.SECONDS)
		                            .writeTimeout(20, TimeUnit.SECONDS)
		                            .build();
		                }
		            }
		        }
		        return mOkHttpClient;
		    }
		
		    /**
		     * 拦截器
		     */
		    private static class MyIntercepter implements Interceptor {
		        @Override
		        public Response intercept(Chain chain) throws IOException {
		            Request request = chain.request();
		            if (!UIUtils.isNetworkConnected()) {
		                Toast.makeText(MyApplication.getContext(), "暂无网络", Toast.LENGTH_SHORT).show();
		                request = request.newBuilder()
		                        .cacheControl(CacheControl.FORCE_CACHE)//无网络时只从缓存中读取
		                        .build();
		            }
		
		            Response response = chain.proceed(request);
		            if (UIUtils.isNetworkConnected()) {
		                int maxAge = 60 * 60; // 有网络时 设置缓存超时时间1个小时
		                response.newBuilder()
		                        .removeHeader("Pragma")
		                        .header("Cache-Control", "public, max-age=" + maxAge)
		                        .build();
		            } else {
		                int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
		                response.newBuilder()
		                        .removeHeader("Pragma")
		                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
		                        .build();
		            }
		            return response;
		        }
		    }
		
		
		    /**
		     * 自动管理Cookies
		     */
		    private static class CookiesManager implements CookieJar {
		        private final PersistentCookieStore cookieStore = new PersistentCookieStore(MyApplication.getContext());
		
		        @Override
		        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
		            if (cookies != null && cookies.size() > 0) {
		                for (Cookie item : cookies) {
		                    cookieStore.add(url, item);
		                }
		            }
		        }
		
		
		        @Override
		        public List<Cookie> loadForRequest(HttpUrl url) {
		            List<Cookie> cookies = cookieStore.get(url);
		            return cookies;
		        }
		    }
		}


* NetUtils（所有的网络请求都会放在这个里面，包含Service

		public class NetUtils extends Retrofit2Utils {
		
		    private static final MyService mService = getRetrofit().create(MyService.class);
		
		    public static Call<MoviesBean> getTop250Movies(int start, int count) {
		        return mService.getTop250Movies(start, count);
		    }
		
		    private interface MyService {
		
		        /**
		         * Top250
		         */
		        @GET("/v2/movie/top250")
		        Call<MoviesBean> getTop250Movies(@Query("start") int start, @Query("count") int count);
		
		        @GET("/v2/movie/in_theaters")
		        Call<MoviesBean> getTheatersMovies(@Query("city") String city);
		
		        /**
		         * /v2/movie/search?q=张艺谋 GET /v2/movie/search?tag=喜剧
		         */
		
		        @GET("/v2/movie/search")
		        Call<MoviesBean> getSearchMovies(@QueryMap Map<String, String> params);
		
		    }
		
		}


* MainActivity 使用


	    private void loadData() {
	        Call<MoviesBean> call = NetUtils.getTop250Movies(0, 20);
			//MoviesBean moviesBean = call.execute().body();//同步请求
	        call.enqueue(new Callback<MoviesBean>() {
	            @Override
	            public void onResponse(Call<MoviesBean> call, Response<MoviesBean> response) {
	                if (response.isSuccessful()) {
	                    MoviesBean body = response.body();
	                    lvMovies.setAdapter(new MovieAdapter(MainActivity.this, body.getSubjects()));
	                }
	
	            }
	
	            @Override
	            public void onFailure(Call<MoviesBean> call, Throwable t) {
	
	            }
	        });
	    }




