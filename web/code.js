/* jshint esversion: 6 */

document.getElementById('searchbutton').onclick = () => {
    fetch("/search?q=" + document.getElementById('searchbox').value)
        .then((response) => response.json())
        .then((data) => {
            document.getElementById("responsesize").innerHTML =
                data.length == 0 ? "<p> <i> No web page contains the query word. </i> </p>" :
                    "<p>" + data.length + " websites retrieved</p>";
            if (data.length === 0) {
                document.getElementById("urllist").style.display="none";
            } else {
                document.getElementById("urllist").style.display= "block";
                let results = data.map((page) =>
                    `<li><a href="${page.url}">${page.title}</a></li>`)
                    .join("\n");
                document.getElementById("urllist").innerHTML = `<ul>${results}</ul>`;
            }
        });
};