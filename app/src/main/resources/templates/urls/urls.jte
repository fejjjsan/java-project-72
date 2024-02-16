@import hexlet.code.dto.UrlsPage
@import hexlet.code.util.NamedRoutes

@param UrlsPage page

@template.layout.page(content = @`
    @if(page.getUrls() == null)
        <p>В базе пусто</p>
    @endif
    @if (page.getMessage() != null)
        <div class="rounded-0 m-0 alert alert-dismissible fade show alert-primary" role="alert">
            <p class="m-0">${page.getMessage().toString()}</p>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    @endif
    <table class="table table-bordered">
        <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Name</th>
                <th scope="col">Last check</th>
                <th scope="col">Status code</th>
            </tr>
        </thead>
        <tbody>
                @if(page.getUrls() != null)
                    @for(var url : page.getUrls())
                        <tr>
                        <th scope="row">${url.getId()}</th>
                        <td><a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a></td>
                        @if(page.getChecks() != null && page.getChecks().containsKey(url.getId()))
                            <td>${page.formatTimestamp(page.getChecks().get(url.getId()).getCreatedAt())}</td>
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
`)
