@import hexlet.code.dto.UrlPage

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
`)
