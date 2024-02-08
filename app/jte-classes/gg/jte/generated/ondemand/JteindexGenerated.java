package gg.jte.generated.ondemand;
import hexlet.code.util.NamedRoutes;
import hexlet.code.dto.MainPage;
public final class JteindexGenerated {
	public static final String JTE_NAME = "index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,5,5,6,6,8,8,10,10,10,13,13,17,17,17,17,17,17,17,17,17,30,30,30,30,30,3,3,3,3};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, MainPage page) {
		jteOutput.writeContent("\n");
		gg.jte.generated.ondemand.layout.JtepageGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n    <section>\n        ");
				if (page.getMessage() != null) {
					jteOutput.writeContent("\n            <div class=\"rounded-0 m-0 alert alert-dismissible fade show alert-danger\" role=\"alert\">\n                <p class=\"m-0\">");
					jteOutput.setContext("p", null);
					jteOutput.writeUserContent(page.getMessage().toString());
					jteOutput.writeContent("</p>\n                <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Close\"></button>\n            </div>\n        ");
				}
				jteOutput.writeContent("\n        <div class=\"container-fluid text-center bg-dark text-white pt-5\">\n            <h1>Анализатор страницы</h1>\n            <p>Бесплатно проверяйте сайты на SEO пригодность</p>\n            <form");
				var __jte_html_attribute_0 = NamedRoutes.urlsPath();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
					jteOutput.writeContent(" action=\"");
					jteOutput.setContext("form", "action");
					jteOutput.writeUserContent(__jte_html_attribute_0);
					jteOutput.setContext("form", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" method=\"post\" class=\"pb-5\">\n                <div class=\"row\">\n                    <div class=\"col\">\n                        <input id=\"url-input\" type=\"text\" required name=\"url\" class=\"form-control\" placeholder=\"Cсылка\">\n                        <label for=\"url-input\">Ссылка</label>\n                    </div>\n                    <div class=\"col-auto\">\n                        <button class=\"btn btn-primary\" type=\"submit\">Проверить</button>\n                    </div>\n                </div>\n            </form>\n        </div>\n    </section>\n");
			}
		});
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		MainPage page = (MainPage)params.get("page");
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
