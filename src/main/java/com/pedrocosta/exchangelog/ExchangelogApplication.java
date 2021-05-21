package com.pedrocosta.exchangelog;

import com.pedrocosta.exchangelog.utils.MessageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@SpringBootApplication
public class ExchangelogApplication {

//	@Autowired
//	private static RestTemplate fixerRequester;
	
	public static void main(String[] args) {
		SpringApplication.run(ExchangelogApplication.class, args);
	}

	@Bean
	public static MessageProperties messageSource() {
		var source = new ResourceBundleMessageSource();
		source.setBasenames("label/messages");
		source.setUseCodeAsDefaultMessage(true);
		MessageProperties properties = new MessageProperties();
		properties.setSource(source);
		return properties;
	}
	
//	@Bean
//    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        return builder.build();
//    }
	
//	@Bean
//    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
//        return args -> {
        	
//        	RestResponse r = new RestResponse();
//        	r.generate();
//        	String fullUrl = r.getFullUrl("latest", "star_date=2020-09-05&end_date=2020-10-01");
//
//        	System.out.println(fullUrl);
        	
//        	JSONObject jsonObj = new JSONObject(getJson());
//        	
//        	System.out.println(getJson());
//        	
//        	System.out.println();
//        	
//        	System.out.println(jsonObj.toString());
        	
//        	ObjectMapper mapper = new ObjectMapper();
//        	JSONObject jsonObj = mapper.readValue(getJson(), JSONObject.class);
        	
//        	jsonObj.print();
//        	
//        	LinkedHashMap map = (LinkedHashMap) jsonObj.get("error");
//        	
//        	Set<Entry> entries = map.entrySet();
//    		
//    		for (Entry entry : entries) {
//    			
//    			System.out.println("---> " + entry.getValue().getClass().getTypeName() + " " + entry.getKey() + " : " + entry.getValue());
//    		}

//			RestResponse rest = new RestResponse("fixer");
//			String s = restTemplate.getForObject(rest.getFullUrl("latest", null), String.class);
//        	String s = restTemplate.getForObject("http://data.fixer.io/api/latest?access_key=7a902ff2b94ec3916524bd4aa57e654d"
//    				, String.class);

//    		System.out.println(s);
//
//			JSONObject jsonObj = new JSONObject(s);
//
//			System.out.println(jsonObj.toString());

//            Object obj = restTemplate.getForObject(
//                    "https://gturnquist-quoters.cfapps.io/api/random", Object.class);
//            System.out.println("Resultado da Chamada REST: " + obj.toString());
//        };
//    }
	
	private String getJson() {
		return "{\"name\":\"John\",\"age\":30,\"cars\":[\"Ford\",\"BMW\",\"Fiat\"]}";
//		return "{\"success\":true,\"timeseries\":true,\"start_date\":\"2012-05-01\",\"end_date\":\"2012-05-03\",\"base\":\"EUR\",\"rates\":{\"2012-05-01\":{\"USD\":1.322891,\"AUD\":1.278047,\"CAD\":1.302303},\"2012-05-02\":{\"USD\":1.315066,\"AUD\":1.274202,\"CAD\":1.299083},\"2012-05-03\":{\"USD\":1.314491,\"AUD\":1.280135,\"CAD\":1.296868}}}";
//		return "{\"success\":true,\"timestamp\":1601553845,\"base\":\"EUR\",\"date\":\"2020-10-01\",\"rates\":{\"AED\":4.321476,\"AFN\":90.38481,\"ALL\":124.214042,\"AMD\":574.650853,\"ANG\":2.110329,\"AOA\":734.625971,\"ARS\":89.558408,\"AUD\":1.633156,\"AWG\":2.117834,\"AZN\":1.996841,\"BAM\":1.958092,\"BBD\":2.373802,\"BDT\":99.690385,\"BGN\":1.95883,\"BHD\":0.443709,\"BIF\":2272.477925,\"BMD\":1.176574,\"BND\":1.602611,\"BOB\":8.118073,\"BRL\":6.565413,\"BSD\":1.175638,\"BTC\":0.000108,\"BTN\":86.011754,\"BWP\":13.607917,\"BYN\":3.057546,\"BYR\":23060.858311,\"BZD\":2.369798,\"CAD\":1.562991,\"CDF\":2306.085794,\"CHF\":1.079143,\"CLF\":0.033285,\"CLP\":918.430864,\"CNY\":7.989294,\"COP\":4501.608965,\"CRC\":709.537736,\"CUC\":1.176574,\"CUP\":31.179222,\"CVE\":110.390199,\"CZK\":26.917618,\"DJF\":209.295585,\"DKK\":7.441868,\"DOP\":68.625998,\"DZD\":151.766392,\"EGP\":18.541659,\"ERN\":17.648746,\"ETB\":43.255373,\"EUR\":1,\"FJD\":2.507103,\"FKP\":0.907643,\"GBP\":0.907521,\"GEL\":3.853268,\"GGP\":0.907643,\"GHS\":6.813056,\"GIP\":0.907643,\"GMD\":60.945179,\"GNF\":11513.958333,\"GTQ\":9.14956,\"GYD\":245.967915,\"HKD\":9.118758,\"HNL\":28.950711,\"HRK\":7.562547,\"HTG\":79.180457,\"HUF\":359.967069,\"IDR\":17442.480219,\"ILS\":4.027882,\"IMP\":0.907643,\"INR\":85.95815,\"IQD\":1403.561608,\"IRR\":49539.665676,\"ISK\":162.202738,\"JEP\":0.907643,\"JMD\":166.441474,\"JOD\":0.834197,\"JPY\":124.083855,\"KES\":127.717081,\"KGS\":93.523665,\"KHR\":4819.044973,\"KMF\":495.514555,\"KPW\":1058.913308,\"KRW\":1366.332436,\"KWD\":0.360197,\"KYD\":0.979811,\"KZT\":505.177233,\"LAK\":10843.482668,\"LBP\":1777.608498,\"LKR\":217.500473,\"LRD\":234.108817,\"LSL\":19.955028,\"LTL\":3.474118,\"LVL\":0.711698,\"LYD\":1.615639,\"MAD\":10.865354,\"MDL\":19.922403,\"MGA\":4581.016095,\"MKD\":61.747129,\"MMK\":1537.199632,\"MNT\":3354.261811,\"MOP\":9.385445,\"MRO\":420.037469,\"MUR\":46.942373,\"MVR\":18.131118,\"MWK\":883.3623,\"MXN\":25.692831,\"MYR\":4.884546,\"MZN\":85.083965,\"NAD\":19.966958,\"NGN\":447.933881,\"NIO\":40.947918,\"NOK\":10.898091,\"NPR\":137.620073,\"NZD\":1.768115,\"OMR\":0.452958,\"PAB\":1.175738,\"PEN\":4.237087,\"PGK\":4.166107,\"PHP\":56.987382,\"PKR\":194.160826,\"PLN\":4.488455,\"PYG\":8212.05286,\"QAR\":4.283924,\"RON\":4.87431,\"RSD\":117.586531,\"RUB\":90.726715,\"RWF\":1147.085802,\"SAR\":4.412911,\"SBD\":9.60869,\"SCR\":21.153657,\"SDG\":65.093958,\"SEK\":10.478736,\"SGD\":1.602129,\"SHP\":0.907643,\"SLL\":11553.960633,\"SOS\":685.942615,\"SRD\":16.653217,\"STD\":24747.844847,\"SVC\":10.287536,\"SYP\":602.508317,\"SZL\":19.551825,\"THB\":37.132706,\"TJS\":12.139281,\"TMT\":4.11801,\"TND\":3.252122,\"TOP\":2.716127,\"TRY\":9.084681,\"TTD\":7.986291,\"TWD\":33.978054,\"TZS\":2726.959554,\"UAH\":33.322541,\"UGX\":4365.232094,\"USD\":1.176574,\"UYU\":49.960025,\"UZS\":12134.832508,\"VEF\":11.751034,\"VND\":27263.616201,\"VUV\":133.732319,\"WST\":3.084455,\"XAF\":656.699004,\"XAG\":0.049429,\"XAU\":0.000619,\"XCD\":3.179751,\"XDR\":0.835244,\"XOF\":656.704591,\"XPF\":119.951616,\"YER\":294.55529,\"ZAR\":19.527429,\"ZMK\":10590.584356,\"ZMW\":23.571933,\"ZWL\":378.857348}}";
	}
	
	private String getJsonError() {
		return "{\"success\":false,\"error\":{\"code\":101,\"type\":\"invalid_access_key\",\"info\":\"You have not supplied a valid API Access Key. [Technical Support: support@apilayer.com]\"}}";
	}

}
