@import hexlet.code.dto.UrlPage
@import hexlet.code.util.NamedRoutes

@param UrlPage page

@template.layout.page(content = @`
    <table>
        <tr>
            <th>id</th>
            <td>${page.getUrl().getId()}</td>
        </tr>
        <tr>
            <th>name</th>
            <td>${page.getUrl().getName()}</td>
        </tr>
        <tr>
            <th>createAt</th>
            <td>${page.getUrl().getCreatedAt().toString()}</td>
        </tr>
    </table>
    <form action="${NamedRoutes.checksPath(page.getUrl().getId())}" method="post">
        <input class="btn btn-primary" type="submit" >
    </form>
    @if (page.getChecks() != null)
        <table class="table table-bordered">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">title</th>
                <th scope="col">h1</th>
                <th scope="col">description</th>
                <th scope="col">status code</th>
                <th scope="col">created at</th>
            </tr>
            </thead>
            <tbody>
            @for(var check : page.getChecks())
            <tr>
                <th scope="row">${check.getId()}</th>
                <td>${check.getTitle()}</td>
                <td>${check.getH1()}</td>
                <td>${check.getDescription()}</td>
                <td>${check.getStatusCode()}</td>
                <td>${check.getCreatedAt().toString()}</td>
            </tr>
            @endfor
            </tbody>
        </table>
    @endif
`)


