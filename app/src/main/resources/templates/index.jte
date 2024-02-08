@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.MainPage

@param MainPage page

@template.layout.page(
content = @`
    <section>
        @if(page.getMessage() != null)
            <div class="rounded-0 m-0 alert alert-dismissible fade show alert-danger" role="alert">
                <p class="m-0">${page.getMessage().toString()}</p>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        @endif
        <div class="container-fluid text-center bg-dark text-white pt-5">
            <h1>Анализатор страницы</h1>
            <p>Бесплатно проверяйте сайты на SEO пригодность</p>
            <form action="${NamedRoutes.urlsPath()}" method="post" class="pb-5">
                <div class="row">
                    <div class="col">
                        <input id="url-input" type="text" required name="url" class="form-control" placeholder="Cсылка">
                        <label for="url-input">Ссылка</label>
                    </div>
                    <div class="col-auto">
                        <button class="btn btn-primary" type="submit">Проверить</button>
                    </div>
                </div>
            </form>
        </div>
    </section>
`)