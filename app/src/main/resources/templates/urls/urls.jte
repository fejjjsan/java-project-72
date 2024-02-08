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
    @for(var url: page.getUrls())
        <p>${url.getId()}</p>
        <a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a>
    @endfor
`)
