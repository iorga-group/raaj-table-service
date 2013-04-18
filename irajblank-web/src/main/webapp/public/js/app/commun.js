browser,versionBrowser;

function checkBrowser()
{
	var detect = navigator.userAgent.toLowerCase();
	
	if (checkIt('konqueror')) browser = "Konqueror"
	else if (checkIt('safari')) browser = "Safari"
	else if (checkIt('omniweb')) browser = "OmniWeb"
	else if (checkIt('opera')) browser = "Opera"
	else if (checkIt('webtv')) browser = "WebTV";
	else if (checkIt('icab')) browser = "iCab"
	else if (checkIt('msie')) browser = "Internet Explorer"
	else if (!checkIt('compatible'))
	{
		browser = "Netscape Navigator"
		version = detect.charAt(8);
	}
	else browser = "An unknown browser";

	if (!version) version = detect.charAt(place + thestring.length);
}

function checkIt(string)
{
	var detect = navigator.userAgent.toLowerCase();
	place = detect.indexOf(string) + 1;
	thestring = string;
	return place;
}