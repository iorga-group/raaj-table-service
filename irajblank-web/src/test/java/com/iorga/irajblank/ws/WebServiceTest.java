package com.iorga.irajblank.ws;

import java.io.File;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(Arquillian.class)
public class WebServiceTest {
	private static final String WEBSERVICE_PREFIX = "/api";

	@Deployment
	public static WebArchive createDeployment() {
		final MavenDependencyResolver resolver = DependencyResolvers
			.use(MavenDependencyResolver.class)
			.loadMetadataFromPom("pom.xml")
			.goOffline();

		return ShrinkWrap.create(WebArchive.class, "test.war")
			.addAsLibraries(resolver.artifacts(
					"org.jboss.resteasy:resteasy-jaxrs",
					"org.jboss.resteasy:resteasy-jackson-provider"
//					"org.slf4j:slf4j-log4j12"	// Used for ClientRequest at runtime
				).resolveAsFiles())
			.addAsResource(new File("src/test/resources/WebServiceTest-log4j.properties"), "log4j.properties")
			.addAsWebInfResource(new File("src/test/resources/WebServiceTest-web.xml"), "web.xml");
//		return ShrinkWrap.create(WebArchive.class, "test.war")
//			.addAsLibraries(new File("target/test-libs/resteasy-jaxrs.jar"))
////			.addAsLibraries(new File("target/test-libs/resteasy-cdi.jar"))
//			.addAsLibraries(new File("target/test-libs/resteasy-jaxb-provider.jar"))
//			.addAsLibraries(new File("target/test-libs/resteasy-jettison-provider.jar"))
//			.addAsLibraries(new File("target/test-libs/jettison.jar"))
//			.addAsLibraries(new File("target/test-libs/stax-api.jar"))
//			.addAsLibraries(new File("target/test-libs/commons-logging.jar"))	// Used for ClientRequest at runtime
////			.addAsLibraries(new File("target/test-libs/weld-api.jar"))
////			.addAsLibraries(new File("target/test-libs/weld-core.jar"))
////			.addAsLibraries(new File("target/test-libs/weld-servlet.jar"))
////			.addAsLibraries(new File("target/test-libs/weld-spi.jar"))
//			.addAsWebInfResource(new File("src/test/resources/WebServiceTest-web.xml"), "web.xml");
////			.addAsResource(EmptyAsset.INSTANCE, "beans.xml")
////			.addAsResource(new File("src/test/resources/WebServiceTest-context.xml"), "context.xml");
	}

//	@ArquillianResource
//	private URL deploymentUrl;
	private static String deploymentUrl = "http://localhost:8080/test";

	@XmlRootElement
	public static class XmlBean {
		private String field;

		public String getField() {
			return field;
		}
		public void setField(final String field) {
			this.field = field;
		}
	}

	@Path("/testxml")
	public static class XmlWebService {
		@POST
		@Path("/post")
		public String post(final XmlBean bean) {
			return bean.getField();
		}
	}

	@Test
	public void testXmlPost() throws Exception {
		final ClientRequest request = new ClientRequest(deploymentUrl.toString() + WEBSERVICE_PREFIX + "/testxml/post")
			.header("Accept", MediaType.APPLICATION_JSON)
			.body(MediaType.APPLICATION_JSON_TYPE, "{\"field\":\"test\"}");
		final ClientResponse<String> clientResponse = request.post(String.class);

		Assert.assertEquals(200, clientResponse.getStatus());
		Assert.assertEquals("test", clientResponse.getEntity());
	}


	public static class Bean {
		private String field;

		public String getField() {
			return field;
		}
		public void setField(final String field) {
			this.field = field;
		}
	}

	@Path("/testbean")
	public static class BeanWebService {
		@POST
		@Path("/post")
		public String post(final Bean bean) {
			return bean.getField();
		}
	}

	@Test
	public void testBeanPost() throws Exception {
		final ClientRequest request = new ClientRequest(deploymentUrl.toString() + WEBSERVICE_PREFIX + "/testbean/post")
			.header("Accept", MediaType.APPLICATION_JSON)
			.body(MediaType.APPLICATION_JSON_TYPE, "{\"field\":\"test\"}");
		final ClientResponse<String> clientResponse = request.post(String.class);

		Assert.assertEquals(200, clientResponse.getStatus());
		Assert.assertEquals("test", clientResponse.getEntity());
	}


	@JsonIgnoreProperties("field2")
	public static class IgnoreMoreBean {
		private String field;

		public String getField() {
			return field;
		}
		public void setField(final String field) {
			this.field = field;
		}
	}

	@Path("/testbeanpostmore")
	public static class BeanPostMoreWebService {
		@POST
		@Path("/post")
		public String post(final IgnoreMoreBean bean) {
			return bean.getField();
		}
	}

	@Test
	public void testBeanPostMore() throws Exception {
		final ClientRequest request = new ClientRequest(deploymentUrl.toString() + WEBSERVICE_PREFIX + "/testbeanpostmore/post")
			.header("Accept", MediaType.APPLICATION_JSON)
			.body(MediaType.APPLICATION_JSON_TYPE, "{\"field\":\"test\",\"field2\":\"test2\"}");
		final ClientResponse<String> clientResponse = request.post(String.class);

		Assert.assertEquals(200, clientResponse.getStatus());
		Assert.assertEquals("test", clientResponse.getEntity());
	}
	

	public static class IgnoreLessBean {
		private String field;
		private String field2;

		public String getField() {
			return field;
		}
		public void setField(final String field) {
			this.field = field;
		}
		public String getField2() {
			return field2;
		}
		public void setField2(String field2) {
			this.field2 = field2;
		}
	}

	@Path("/testbeanpostless")
	public static class BeanPostLessWebService {
		@POST
		@Path("/post")
		public String post(final IgnoreLessBean bean) {
			return bean.getField();
		}
	}

	@Test
	public void testBeanPostLess() throws Exception {
		final ClientRequest request = new ClientRequest(deploymentUrl.toString() + WEBSERVICE_PREFIX + "/testbeanpostless/post")
			.header("Accept", MediaType.APPLICATION_JSON)
			.body(MediaType.APPLICATION_JSON_TYPE, "{\"field\":\"test\"}");
		final ClientResponse<String> clientResponse = request.post(String.class);

		Assert.assertEquals(200, clientResponse.getStatus());
		Assert.assertEquals("test", clientResponse.getEntity());
	}
}
