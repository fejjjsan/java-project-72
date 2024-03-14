@import hexlet.code.dto.MainPage
@import hexlet.code.dto.UrlsPage
@import hexlet.code.util.NamedRoutes

@param UrlsPage page

@template.layout.page(content = @`
    @if (page.getMessage() != null)
        <div class="rounded-0 m-0 alert alert-dismissible fade show alert-success" role="alert">
            <p class="m-0">${page.getMessage().toString()}</p>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    @endif
    <section>
        <div class="container-lg mt-5">
            <h1>Сaйты</h1>
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Имя</th>
                    <th scope="col">Последняя проверка</th>
                    <th scope="col">Код ответа</th>
                </tr>
                </thead>
                <tbody>
                @if(page.getUrls() != null)
                    @for(var url : page.getUrls())
                        <tr>
                            <th scope="row">${url.getId()}</th>
                            <td><a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a></td>
                            @if(page.getChecks() != null && page.getChecks().containsKey(url.getId()))
                                <td>${MainPage.formatTimestamp(page.getChecks().get(url.getId()).getCreatedAt())}</td>
                                <td>${page.getChecks().get(url.getId()).getStatusCode()}</td>
                            @else
                                <td></td>
                                <td></td>
                            @endif
                        </tr>
                    @endfor
                @endif
                </tbody>
            </table>
        </div>
    </section>

`)
