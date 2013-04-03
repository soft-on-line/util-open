package org.open.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 国家名称常量定义功能类 
 * @author wyp
 * @author ibm
 * @version $Id:$
 */
public class CountryUtil 
{
	/**
	 * 国家数据类型定义类
	 * @author wyp
	 * @author ibm
	 * @version $Id:$
	 */
	public static final class Country
	{
		private static Map<String,Country> countryMap = new HashMap<String,Country>();
		
		private String english = "";
		private String chinese = "";
		private String capital = "";
		private String continent = "";
		private String abbreviation = "";
		
		/**
		 * Afghanistan 阿富汗 喀布尔 西南亚 AF 
		 */
		public static final Country Afghanistan = new Country("Afghanistan","阿富汗","喀布尔","西南亚","AF");
		public static final Country Albania = new Country("Albania","阿尔巴尼亚","地拉那","欧洲","AL");
		public static final Country Algeria = new Country("Algeria","阿尔及利亚","阿尔及尔","北非洲","DZ");
		public static final Country AmericanSamoa = new Country("American Samoa","美属萨摩亚群岛","帕果帕果","大洋洲","AS");
		public static final Country Andorra = new Country("Andorra","安道尔","安道尔","欧洲","AD");
		public static final Country Angola = new Country("Angola","安哥拉","罗安达","非洲","AO");
		public static final Country Anguilla = new Country("Anguilla","安圭拉","瓦利","美洲","AI");
		public static final Country AntiguaandBarbuda = new Country("Antigua and Barbuda","安提瓜和巴布达","圣约翰","美洲","");
		public static final Country Argentina = new Country("Argentina","阿根廷","布宜诺斯艾利斯","南美洲","AR");
		public static final Country Armenia = new Country("Armenia","亚美尼亚","埃里温","亚洲","AM");
		public static final Country Australia = new Country("Australia","澳大利亚","堪培拉","大洋洲","AU");
		public static final Country Austria = new Country("Austria","奥地利","维也纳","欧洲","AT");
		public static final Country Azerbaijan = new Country("Azerbaijan","阿塞拜疆","巴库","亚洲","AZ");
		public static final Country Bahamas = new Country("Bahamas","巴哈马","拿骚","北美洲","BS");
		public static final Country Bahrain = new Country("Bahrain","巴林","麦纳麦","亚洲","BH");
		public static final Country Bangladesh = new Country("Bangladesh","孟加拉","达卡","亚洲","BD");
		public static final Country Barbados = new Country("Barbados","巴巴多斯","布里奇敦","北美洲","BB");
		public static final Country Belarus = new Country("Belarus","白俄罗斯","明斯克","欧洲","");
		public static final Country Belize = new Country("Belize","伯利兹","贝尔莫潘","北美洲","BZ");
		public static final Country Belgium = new Country("Belgium","比利时","布鲁塞尔","欧洲","BE");
		public static final Country Benin = new Country("Benin","贝宁","波多诺伏","非洲","BJ");
		public static final Country Bermuda = new Country("Bermuda","百慕大","汉密尔顿","北美洲","BM");
		public static final Country Bhutan = new Country("Bhutan","不丹","廷布","亚洲","BT");
		public static final Country Bolivia = new Country("Bolivia","玻利维亚","苏克雷","南美洲","BO");
		public static final Country Bosnia_Herzegovina = new Country("Bosnia & Herzegovina","波斯尼亚和黑塞哥维那","萨拉热窝","欧洲","BA");
		public static final Country Botswana = new Country("Botswana","博茨瓦纳","哈博罗内","非洲","BW");
		public static final Country Brazil = new Country("Brazil","巴西","巴西利亚","南美洲","BR");
		public static final Country Brunei = new Country("Brunei","文莱","斯里巴加湾市","亚洲","BN");
		public static final Country Bulgaria = new Country("Bulgaria","保加利亚","索非亚","欧洲","BG");
		public static final Country BurkinaFaso = new Country("Burkina Faso","布基纳法索","瓦加杜古","非洲","BF");
		public static final Country Burundi = new Country("Burundi","布隆迪","布琼布拉","非洲","BI");
		public static final Country Cambodia = new Country("Cambodia","柬埔寨","金边","亚洲","KH");
		public static final Country Cameroon = new Country("Cameroon","喀麦隆","雅温得","非洲","CM");
		public static final Country Canada = new Country("Canada","加拿大","渥太华","北美洲","CA");
		public static final Country Capeverdeislands = new Country("Cape verde islands","佛得角群岛","普拉亚","非洲","CV");
		public static final Country CaymanIslands = new Country("Cayman Islands","开曼群岛","乔治敦","美洲","KY");
		public static final Country CentralAfrica = new Country("Central Africa","中非共和国","班吉","非洲","CF");
		public static final Country Chad = new Country("Chad","乍得","恩贾梅纳","非洲","TD");
		public static final Country Chile = new Country("Chile","智利","圣地亚哥","南美洲","CL");
		public static final Country China = new Country("China","中国","北京","亚洲","CN");
		public static final Country HongKong = new Country("Hong Kong","香港","","亚洲","HK");
		public static final Country Macau = new Country("Macau","澳门","","亚洲","MO");
		public static final Country Taiwan = new Country("Taiwan","台湾","台北","亚洲","TW");
		public static final Country CookIslands = new Country("Cook Islands","库克群岛","阿瓦鲁阿","大洋洲","CK");
		public static final Country Colombia = new Country("Colombia","哥伦比亚","波哥大","南美洲","CO");
		public static final Country Comoros = new Country("Comoros","科摩罗群岛","莫罗尼","非洲","KM");
		public static final Country TheRepublicofCongo = new Country("The Republic of Congo","刚果共和国/刚果(布)","布拉柴维尔","非洲","CG");
		public static final Country TheDemocraticRepublicOfCongo = new Country("The Democratic Republic Of Congo","刚果民主共和国/刚果(金)","金沙萨","非洲","ZR");
		public static final Country Costarica = new Country("Costarica","哥斯达黎加","圣何塞","北美洲","CR");
		public static final Country IvoryCoast = new Country("Cote d Ivoire","科特迪瓦","亚穆苏克罗","非洲","CI");
		public static final Country Croatia = new Country("Croatia","克罗地亚","萨格勒布","欧洲","HR");
		public static final Country Cuba = new Country("Cuba","古巴","哈瓦那","北美洲","CU");
		public static final Country Cyprus = new Country("Cyprus","塞浦路斯","尼科西亚","亚洲","CY");
		public static final Country CzechRepublic = new Country("Czech Republic","捷克共和国","布拉格","欧洲","CZ");
		public static final Country Denmark = new Country("Denmark","丹麦","哥本哈根","欧洲","DK");
		public static final Country Djibouti = new Country("Djibouti","吉布提","吉布提","非洲","DJ");
		public static final Country Dominica = new Country("Dominica","多米尼克","罗索","北美洲","DM");
		public static final Country DominicaRepublic = new Country("Dominica Republic","多米尼加共和国","圣多明各","北美洲","DO");
		public static final Country EastTimor = new Country("East Timor","东帝汶","帝力","亚洲","TP");
		public static final Country Ecuador = new Country("Ecuador","厄瓜多尔","基多","南美洲","EC");
		public static final Country Egypt = new Country("Egypt","埃及","开罗","非洲","EG");
		public static final Country ElSalvador = new Country("El Salvador","萨尔瓦多","圣萨尔瓦多","美洲","SV");
		public static final Country EquatorialGuinea = new Country("Equatorial Guinea","赤道几内亚","马拉博","西非洲","GQ");
		public static final Country Eritrea = new Country("Eritrea","厄立特里亚","阿斯马拉","非洲","ER");
		public static final Country Estonia = new Country("Estonia","爱沙尼亚","塔林","欧洲","EE");
		public static final Country Ethiopia = new Country("Ethiopia","埃塞俄比亚","亚的斯亚贝巴","非洲","ET");
		public static final Country FalklandIslands_IslasMalvinas = new Country("Falkland Islands/Islas Malvinas","马尔维那斯群岛","斯坦利港","南美洲","FK");
		public static final Country FijiIslands = new Country("Fiji Islands","斐济","苏瓦","","");
		public static final Country Finland = new Country("Finland","芬兰","赫尔辛基","欧洲","FI");
		public static final Country France = new Country("France","法国","巴黎","欧洲","FR");
		public static final Country FrenchGuiana = new Country("French Guiana","法属圭亚那","卡宴","南美洲","GF");
		public static final Country FrenchPolynesia = new Country("French Polynesia","法属玻利尼西亚","帕皮提","大洋洲","PF");
		public static final Country Gabon = new Country("Gabon","加蓬","利伯维尔","非洲","GA");
		public static final Country Gambia = new Country("Gambia","冈比亚","班珠尔","非洲","GB");
		public static final Country Georgia = new Country("Georgia","格鲁吉亚","第比利斯","亚洲","GE");
		public static final Country Germany_Deutschland = new Country("Germany/Deutschland","德国","柏林","欧洲","DE");
		public static final Country Ghana = new Country("Ghana","加纳","阿克拉","非洲","GH");
		public static final Country Gibraltar = new Country("Gibraltar","直布罗陀","","欧洲","GI");
		public static final Country Greece_HellenicRepublic = new Country("Greece/Hellenic Republic","希腊","雅典","欧洲","GR");
		public static final Country Greenland = new Country("Greenland","格陵兰","努克","美洲","GL");
		public static final Country Grenada = new Country("Grenada","格林纳达","圣乔治","美洲","GD");
		public static final Country Guadeloupe = new Country("Guadeloupe","瓜德罗普","巴斯特尔","美洲","GP");
		public static final Country Guam = new Country("Guam","关岛","阿加尼亚","大洋洲","GU");
		public static final Country Guatemala = new Country("Guatemala","危地马拉","危地马拉","北美洲","GT");
		public static final Country GuineBissau = new Country("Guine Bissau","几内亚比绍","比绍","非洲","GW");
		public static final Country Guinea = new Country("Guinea","几内亚","科纳克里","非洲","GN");
		public static final Country Guyana = new Country("Guyana","圭亚那","乔治敦","南美洲","GY");
		public static final Country Haiti = new Country("Haiti","海地","太子港","北美洲","HT");
		public static final Country Honduras = new Country("Honduras","洪都拉斯","特古西加尔巴","北美洲","HN");
		public static final Country Hungary = new Country("Hungary","匈牙利","布达佩斯","欧洲","HU");
		public static final Country Iceland = new Country("Iceland","冰岛","雷克雅未克","欧洲","IS");
		public static final Country India = new Country("India","印度","新德里","亚洲","IN");
		public static final Country Indonesia = new Country("Indonesia","印度尼西亚","雅加达","亚洲","ID");
		public static final Country Iran = new Country("Iran","伊朗","德黑兰","亚洲","IR");
		public static final Country Iraq = new Country("Iraq","伊拉克","巴格达","亚洲","IQ");
		public static final Country Ireland = new Country("Ireland","爱尔兰","都柏林","欧洲","IE");
		public static final Country Israel = new Country("Israel","以色列","耶路撒冷","亚洲","IL");
		public static final Country Italy = new Country("Italy","意大利","罗马","欧洲","IT");
		public static final Country Jamaica = new Country("Jamaica","牙买加","金斯敦","美洲","JM");
		public static final Country Japan = new Country("Japan","日本","东京","亚洲","JP");
		public static final Country Jordan = new Country("Jordan","约旦","安曼","亚洲","JO");
		public static final Country Kazakhstan = new Country("Kazakhstan","哈萨克斯坦","阿斯塔纳","亚洲","KZ");
		public static final Country Kenya = new Country("Kenya","肯尼亚","内罗华","非洲","KE");
		public static final Country Kiribati = new Country("Kiribati","基里巴斯","塔拉瓦","大洋洲","");
		public static final Country RepublicofKorea = new Country("Republic of Korea","韩国","汉城","亚洲","KP");
		public static final Country TheDemocraticPeoples_RepublicofKorea = new Country("The Democratic People's Republic of Korea","朝鲜","平壤","亚洲","KR");
		public static final Country Kuwait = new Country("Kuwait","科威特","科威特城","亚洲","KW");
		public static final Country KyrghyzRepublic = new Country("Kyrghyz Republic","吉尔吉斯共和国","比什凯克","亚洲","");
		public static final Country Laos = new Country("Laos","老挝","万象","亚洲","LA");
		public static final Country Latvia = new Country("Latvia","拉脱维亚","里加","欧洲","LV");
		public static final Country Lebanon = new Country("Lebanon","黎巴嫩","贝鲁特","亚洲","LB");
		public static final Country Lesotho = new Country("Lesotho","莱索托","马塞卢","南非洲","LS");
		public static final Country Liberia = new Country("Liberia","利比里亚","蒙罗维亚","西非洲","LR");
		public static final Country Libya = new Country("Libya","利比亚","的黎波里","北非洲","LY");
		public static final Country Liechtenstein = new Country("Liechtenstein","列支敦士登","瓦杜兹","欧洲","LI");
		public static final Country Lithuania = new Country("Lithuania","立陶宛","维尔纽斯","欧洲","LT");
		public static final Country Luxembourg = new Country("Luxembourg","卢森堡","卢森堡","欧洲","LU");
		public static final Country Macedonia = new Country("Macedonia","马其顿","斯科普里","欧洲","");
		public static final Country Madagascar = new Country("Madagascar","马达加斯加","塔那那利佛","非洲","MG");
		public static final Country Malawi = new Country("Malawi","马拉维","利隆圭","非洲","MW");
		public static final Country Malaysia = new Country("Malaysia","马来西亚","吉隆坡","亚洲","MY");
		public static final Country Maldives = new Country("Maldives","马尔代夫","马累","亚洲","MV");
		public static final Country Mali = new Country("Mali","马里","巴马科","非洲","ML");
		public static final Country Malta = new Country("Malta","马耳他","瓦莱塔","欧洲","MT");
		public static final Country MarshallIslands = new Country("Marshall Islands","马绍尔群岛","马朱罗","大洋洲","MH");
		public static final Country Martinique = new Country("Martinique","马提尼克岛","法兰西堡","美洲","MQ");
		public static final Country Mauritania = new Country("Mauritania","毛里塔尼亚","努瓦克肖特","非洲","MR");
		public static final Country Mauritius = new Country("Mauritius","毛里求斯","路易港","非洲","MU");
		public static final Country Mexico = new Country("Mexico","墨西哥","墨西哥城","北美洲","MX");
		public static final Country Micronesia = new Country("Micronesia","密克罗尼西亚","帕利基尔","大洋洲","FM");
		public static final Country Moldova = new Country("Moldova","摩尔多瓦","基希讷乌","欧洲","MD");
		public static final Country Monaco = new Country("Monaco","摩纳哥","摩纳哥","欧洲","MC");
		public static final Country Mongolia = new Country("Mongolia","蒙古","乌兰巴托","亚洲","MN");
		public static final Country Montserrat = new Country("Montserrat","蒙特塞拉特","普利茅斯","美洲","MS");
		public static final Country Morocco = new Country("Morocco","摩洛哥","拉巴特","非洲","MA");
		public static final Country Mozambique = new Country("Mozambique","莫桑比克","马普托","非洲","MZ");
		public static final Country Myanmar = new Country("Myanmar","缅甸","仰光","亚洲","");
		public static final Country Namibia = new Country("Namibia","纳米比亚","温得和克","非洲","NA");
		public static final Country Nauru = new Country("Nauru","瑙鲁","亚伦","大洋洲","NR");
		public static final Country Nepal = new Country("Nepal","尼泊尔","加德满都","亚洲","NP");
		public static final Country Netherlands = new Country("Netherlands","荷兰","阿姆斯特丹","欧洲","NL");
		public static final Country NewCaledonia = new Country("New Caledonia","新喀里多尼亚岛","努美阿","大洋洲","NC");
		public static final Country NewZealand = new Country("New Zealand","新西兰","惠灵顿","大洋洲","NZ");
		public static final Country Nicaragua = new Country("Nicaragua","尼加拉瓜","马那瓜","北美洲","NI");
		public static final Country Niger = new Country("Niger","尼日尔","尼亚美","非洲","NE");
		public static final Country Nigeria = new Country("Nigeria","尼日利亚","阿布贾","非洲","NG");
		public static final Country Niue = new Country("Niue","纽埃岛","阿洛菲","大洋洲","NU");
		public static final Country NorthernMarianaIslands = new Country("Northern Mariana Islands","北马里亚纳群岛","塞班岛","大洋洲","MP");
		public static final Country Norway = new Country("Norway","挪威","奥斯路","欧洲","NO");
		public static final Country Oman = new Country("Oman","阿曼","马斯喀特","亚洲","OM");
		public static final Country Pakistan = new Country("Pakistan","巴基斯坦","伊斯兰堡","南亚洲","PK");
		public static final Country Palestine = new Country("Palestine","巴勒斯坦","耶路撒冷","亚洲","");
		public static final Country Palau = new Country("Palau","帕劳","科罗尔","大洋洲","");
		public static final Country Panama = new Country("Panama","巴拿马","巴拿马城","北美洲","PA");
		public static final Country PapuaNewGuinea = new Country("Papua New Guinea","巴布亚新几内亚","莫尔兹比港","大洋洲","PG");
		public static final Country Paraguay = new Country("Paraguay","巴拉圭","亚松森","南美洲","PY");
		public static final Country Peru = new Country("Peru","秘鲁","利马","南美洲","PE");
		public static final Country Philippines = new Country("Philippines","菲律宾","马尼拉","亚洲","PH");
		public static final Country Poland = new Country("Poland","波兰","华沙","欧洲","PL");
		public static final Country Portugal = new Country("Portugal","葡萄牙","里斯本","欧洲","PT");
		public static final Country PuertoRico = new Country("Puerto Rico","波多黎哥","圣胡安","北美洲","PR");
		public static final Country Qatar = new Country("Qatar","卡塔尔","多哈","亚洲","QA");
		public static final Country Reunion = new Country("Reunion","留尼汪岛","圣但尼","非洲","RE");
		public static final Country Romania = new Country("Romania","罗马尼亚","布加勒斯特","欧洲","RO");
		public static final Country Russia = new Country("Russia","俄罗斯","莫斯科","欧洲","RU");
		public static final Country Rwanda = new Country("Rwanda","卢旺达","基加利","非洲","RW");
		public static final Country SaintHelena = new Country("Saint Helena","圣赫勒拿岛","非洲","SH","");
		public static final Country TheFederationofSaintKittsAndNevis = new Country("The Federation of Saint Kitts and Nevis","圣基茨和尼维斯联邦","巴斯特尔","美洲","");
		public static final Country SaintLucia = new Country("Saint Lucia","圣卢西亚","卡斯特里","美洲","LC");
		public static final Country SaintVincentandtheGrenadines = new Country("Saint Vincent and the Grenadines","圣文森特和格林纳丁斯","金斯敦","美洲","");
		public static final Country Samoa = new Country("Samoa","萨摩亚群岛","阿皮亚","大洋洲","WS");
		public static final Country SanMarino = new Country("San Marino","圣马力诺","圣马力诺","欧洲","SM");
		public static final Country SaoTomeandPrincipe = new Country("Sao Tome and Principe","圣多美岛和普林西比岛","圣多美","非洲","ST");
		public static final Country SaudiArabia = new Country("Saudi Arabia","沙特阿拉伯","利雅得","亚洲","SA");
		public static final Country Senegal = new Country("Senegal","塞内加尔","达喀尔","西非洲","SN");
		public static final Country Seychelles = new Country("Seychelles","塞舌尔","维多利亚","非洲","SC");
		public static final Country Sierraleone = new Country("Sierraleone","塞拉利昂","弗里敦","非洲","SL");
		public static final Country Singapore = new Country("Singapore","新加坡","新加坡","亚洲","SG");
		public static final Country Slovakian = new Country("Slovakian","斯洛伐克","布拉提斯拉发","欧洲","SK");
		public static final Country Slovenia = new Country("Slovenia","斯洛文尼亚","卢布尔雅那","欧洲","SI");
		public static final Country SolomonIslands = new Country("Solomon Islands","所罗门群岛","霍尼亚拉","大洋洲","SB");
		public static final Country Somalia = new Country("Somalia","索马里","摩加迪沙","非洲","SO");
		public static final Country SouthAfrica = new Country("South Africa","南非","比勒陀利亚","非洲","ZA");
		public static final Country Spain = new Country("Spain","西班牙","马德里","欧洲","ES");
		public static final Country SriLanka = new Country("Sri Lanka","斯里兰卡","科伦坡","亚洲","LK");
		public static final Country Sudan = new Country("Sudan","苏丹","喀土穆","非洲","SD");
		public static final Country Surinam = new Country("Surinam","苏里南","帕拉马里博","南美洲","SR");
		public static final Country Svalbard = new Country("Svalbard","斯瓦尔巴特群岛","朗伊尔城","欧洲","SJ");
		public static final Country Swaziland = new Country("Swaziland","斯威士兰","姆巴巴内","非洲","SZ");
		public static final Country Sweden = new Country("Sweden","瑞典","斯德哥尔摩","欧洲","SE");
		public static final Country Switzerland = new Country("Switzerland","瑞士","伯尔尼","欧洲","CH");
		public static final Country Syria = new Country("Syria","叙利亚","大马士革","亚洲","SY");
		public static final Country Tajikstan = new Country("Tajikstan","塔吉克斯坦","杜尚别","亚洲","TJ");
		public static final Country Tanzania = new Country("Tanzania","坦桑尼亚","达累斯萨拉姆","非洲","TZ");
		public static final Country Thailand = new Country("Thailand","泰国","曼谷","亚洲","TH");
		public static final Country Togo = new Country("Togo","多哥","洛美","非洲","TG");
		public static final Country TokelauIslands = new Country("Tokelau Islands","托克劳群岛","","大洋洲","TK");
		public static final Country Tonga = new Country("Tonga","汤加","努库阿洛法","大洋洲","TO");
		public static final Country TrinidadandTobago = new Country("Trinidad and Tobago","特立尼达和多巴哥","西班牙港","北美洲","TT");
		public static final Country Tunisia = new Country("Tunisia","突尼斯","突尼斯","非洲","TN");
		public static final Country Turkey = new Country("Turkey","土耳其","安卡拉","亚洲","");
		public static final Country Turkmenistan = new Country("Turkmenistan","土库曼斯坦","阿什哈巴德","亚洲","TM");
		public static final Country Tuvalu = new Country("Tuvalu","图瓦卢","富纳富提","大洋洲","");
		public static final Country Uganda = new Country("Uganda","乌干达","坎帕拉","非洲","UG");
		public static final Country Ukraine = new Country("Ukraine","乌克兰","基辅","欧洲","UA");
		public static final Country UnitedArabEmirates = new Country("United Arab Emirates","阿联酋","阿布扎比","亚洲","AE");
		public static final Country UnitedkingdomofGreatBritainAndNorthernIreland = new Country("United kingdom of Great Britain and Northern Ireland","英国","伦敦","欧洲","UK");
		public static final Country UnitedStatesofAmerica = new Country("United States of America","美国","华盛顿","美洲","US");
		public static final Country Uruguay = new Country("Uruguay","乌拉圭","蒙得维的亚","南美洲","UY");
		public static final Country Uzbekistan = new Country("Uzbekistan","乌兹别克斯坦","塔什干","亚洲","UZ");
		public static final Country RepublicofVanuatu = new Country("Republic of Vanuatu","瓦努阿图","维拉港","大洋洲","");
		public static final Country VaticanCity = new Country("Vatican City","梵蒂冈","梵蒂冈城","欧洲","VA");
		public static final Country Venezuela = new Country("Venezuela","委内瑞拉","加拉加斯","南美洲","VE");
		public static final Country Vietnam = new Country("Vietnam","越南","河内","亚洲","VN");
		public static final Country BritishVirginIslands = new Country("British Virgin Islands","维京岛(英)","罗德城","美洲","VG");
		public static final Country UnitedStatesVirginIslands = new Country("United States Virgin Islands","维京岛(美)","夏洛特·阿马里","美洲","VI");
		public static final Country Wallis_Futuna = new Country("Wallis & Futuna","瓦利斯和富图纳群岛","马塔乌图","大洋洲","WF");
		public static final Country WestSahara = new Country("West Sahara","西撒哈拉","阿尤恩","非洲","EH");
		public static final Country Yemen = new Country("Yemen","也门","萨那","亚洲","YE");
		public static final Country Yugoslavia = new Country("Yugoslavia","南斯拉夫","贝尔格莱德","欧洲","YU");
		public static final Country Zambia = new Country("Zambia","赞比亚","卢萨卡","非洲","ZM/GM");
		public static final Country Zimbabwe = new Country("Zimbabwe","津巴布韦","哈拉雷","非洲","ZW");

		private Country(String english, String chinese, String capital, String continent, String abbreviation)
		{
			this.english = english; 
			this.chinese = chinese;
			this.capital = capital; //首都
			this.continent = continent;
			this.abbreviation = abbreviation;
			
			countryMap.put(english, this);
			countryMap.put(chinese, this);
		}
		
		public String getEnglish() {
			return english;
		}
		
		public String getChinese() {
			return chinese;
		}
		
		public String getCapital() {
			return capital;
		}
		
		public String getContinent() {
			return continent;
		}
		
		public String getAbbreviation() {
			return abbreviation;
		}
		
		public String toString()
		{
			return "[english:"+english+",chinese:"+chinese+",capital:"+",continent:"+continent+",abbreviation"+abbreviation+"]";
		}
	}
	
	public static Country index(String name)
	{
		return Country.countryMap.get(name);
	}
	
	public static Country[] toArray()
	{
		return Country.countryMap.values().toArray(new Country[Country.countryMap.values().size()]);
	}
	
	public static int getCountrySize()
	{
		return Country.countryMap.values().size();
	}

}
