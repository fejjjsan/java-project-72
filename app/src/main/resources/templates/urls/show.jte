@import hexlet.code.dto.UrlPage
@import hexlet.code.util.NamedRoutes
@import java.util.Collections

@param UrlPage page

@template.layout.page(content = @`
    @if (page.getMessage() != null)
        @if (page.getMessage().toString().contains("успешно"))
        <div class="rounded-0 m-0 alert alert-dismissible fade show alert-success" role="alert">
            <p class="m-0">${page.getMessage().toString()}</p>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        @else
        <div class="rounded-0 m-0 alert alert-dismissible fade show alert-danger" role="alert">
            <p class="m-0">${page.getMessage().toString()}</p>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        @endif
    @endif
    <section>
        <div class="container-lg mt-5">
        <h1>Cайт: ${page.getUrl().getName()}</h1>
        <table class="table table-bordered table-hover">
            <tbody>
            <tr>
                <th>ID</th>
                <td>${page.getUrl().getId()}</td>
            </tr>
            <tr>
                <th>Имя</th>
                <td>${page.getUrl().getName()}</td>
            </tr>
            <tr>
                <th>Дата создания</th>
                <td>${page.formatTimestamp(page.getUrl().getCreatedAt())}</td>
            </tr>
            </tbody>
        </table>
    <h2 class="mt-5">Проверки</h2>
    <form action="${NamedRoutes.checksPath(page.getUrl().getId())}" method="post">
        <button type="submit" class="btn btn-primary">Запустить проверку</button>
    </form>
    @if (page.getChecks() != null)
        <table class="table table-bordered table-hover mt-1">
            <thead>
            <tr>
                <th scope="col">ID</th>
                <th scope="col">title</th>
                <th scope="col">h1</th>
                <th scope="col">description</th>
                <th scope="col">Код ответа</th>
                <th scope="col">Дата проверки</th>
            </tr>
            </thead>
            <tbody>
            @for(var check : page.reverseList(page.getChecks()))
            <tr>
                <th scope="row">${check.getId()}</th>
                <td>${check.getTitle()}</td>
                <td>${check.getH1()}</td>
                <td>${check.getDescription()}</td>
                <td>${check.getStatusCode()}</td>
                <td>${page.formatTimestamp(check.getCreatedAt())}</td>
            </tr>
            @endfor
            </tbody>
        </table>
        </div>
    </section>
    @endif
`)


