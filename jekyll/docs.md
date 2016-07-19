---
layout: docs
---

# What is a microsite?

TODO Include tips on how to build ranges of both before, after and around a date
A microsite is an instance of Jekyll, ready to publish a static web page for your new library. Some of the benefits of having these auto-generated web pages are:

- You can write documentation easily in markdown format.
- Templates, layouts, styles and other resources will be able in an external CDN. We can easily change them, and updates will be reflected in every microsite.
- You don't have to deal with the styling.

Currently microsites includes two pages:

- Home: The landing page, the public face of your library.
- Docs (Optional): The page where the documentation of your library should be included. Probably you are watching the `Docs` page of this repo right now.

# How to start?

You only need address a few steps to have a microsite in your repo:

## Copy the template

Copy the content of the directory [Jekyll](https://github.com/47deg/microsites/tree/master/jekyll) locally. If your library is a _SBT_ project you will want to choose `docs/src/jekyll` as the destination folder. (_FYI: We are working on developing a SBT plugin to do that automatically with a SBT task_)


## Set parameters

Edit `_config.yml` file to set the parameters of your microsite:

```
name: <NAME_OF_YOUR_LIBRARY>
description: "<DESCRIPTION_OF_YOUR_LIBRARY>"
github_owner: 47deg
baseurl: /microsites
style: default
docs: false
markdown: redcarpet
collections:
  tut:
    output: true

```

## Fill home content

Edit the file `index.md`:

- Add three technologies used in your libraries:

	```
technologies:
 - scala: ["Scala", "Lorem ipsum dolor sit amet, conse ctetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolo…"]
 - android: ["Android", "Lorem ipsum dolor sit amet, conse ctetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolo…"]
 - database: ["Database", "Lorem ipsum dolor sit amet, conse ctetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolo…"]
 ```

	It will produce somethig like that:

	![](http://rafp.es/1NFaThr)


- Add as many features as you need, in markdown format, inside lists (`*`) as below:

	```markdown
	* ## Feature one
	  Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean mollis, enim quis mollis blandit, orci urna lobortis eros, quis tristique quam tellus sit amet nulla. Vivamus congue est quis magna vehicula lobortis. Pellentesque suscipit lectus eu mi vehicula, sed vehicula lectus porta. Aenean tempor metus ac viverra tempus.

	* ```scala
	object repositories {

		  class Repository[F[_]](implicit D: DataSource[F]) {
			def add[A](a: A): FreeC[F, Option[A]] = D.add(a)
			def getAll[A]: FreeC[F, List[A]] = D.getAll
		  }

		  object Repository {
			implicit def repository[F[_]]
			(implicit D: DataSource[F]): Repository[F] = new Repository[F]
		  }

		}
	``.`

	* ## Feature two
	  Morbi mollis molestie vulputate. Quisque interdum maximus fringilla. Mauris id ligula eu lacus egestas euismod. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras in velit a lacus pellentesque interdum. Suspendisse ipsum massa, convallis in venenatis eleifend, congue vitae tellus

	* ```scala
	  def or[F[_], G[_], H[_]]
	   (f: F ~> H, g: G ~> H):
	   ({type cp[α] = Coproduct[F, G, α]})#cp ~> H =
		  new NaturalTransformation[({type cp[α] =
			Coproduct[F, G, α]})#cp, H] {
			  def apply[A](fa: Coproduct[F, G, A]): H[A] = fa.run match {
				case -\/(ff) => f(ff)
				case \/-(gg) => g(gg)
			  }
		}
	``.`
```

	It will produce somethig like that:

	![](http://rafp.es/1NFbsb5)

## The docs page (optional)

If you want to include a documentation page, you should set the parameter `docs` to `true` in the file `_config.yml`. You also have to write the sections into the file `docs.md` keeping in mind only one detail: headers `#` and `##` not only will produce html `h1` and `h2` items but also will generate items in the sidebar menu.


# Customize your microsite

Although common resources are retrieved from a [CDN](https://github.com/47deg/microsites/tree/cdn), you can customize your microsite easily. Just create a directory in [CDN branch](https://github.com/47deg/microsites/tree/cdn) with the same name that you have set the parameter `style` in the `_config.yml` file. As you can see, the `style` parameter has been set to `default` value, so logos and palette of colours are retrieved from the `default` folder in the branch. Once your folder created, you have to provide these files within:

- `navbar_brand.png` and `sidebar_brand.png`: Logos for home and docs pages.
- `jumbotron_pattern.png`: Background pattern for home's jumbotron.
- `palette.css`: Color rules that overrides default ones.

The proper protocol to address these steps is, firstly create a issue for providing these resources and assign it to the design department, and then set the `style` parameter in your `_config.yml` file.

# Test it locally

You can monitor how looks your microsite in every moment, just running the command `jekyll server` and next open `http://127.0.0.1:4000/<base_url>/` in your browser.

We are assuming you have installed Jekyll, otherwise you can [do it thus](https://jekyllrb.com/docs/installation/): `gem install jekyll nokogiri redcarpet` _(Nokogiri and Redcarpet are required plugins)_.

# Deploy the microsite

## Non-Scala projects

Usually, Github is able to compile Jekyll sites for deploying in `gh-pages` branch so in a general context the only step to do would be pushing the microsite directly in the `gh-pages` of your repo. However, GitHub doesn't compile Jekyll plugins and consequently it'd ignore the feature for retrieving remote layouts from our CDN.

For that reason we have to push static files after compiling:

- Run `jekyll server`.
- Copy de content of the auto-generated folder: `_site` (It should only contain the files `home.html` and `docs.html`).
- Paste both files in the `gh-pages` brach of you repo and push them.

You should be able to open the site: `http://47deg.github.io/<base_url>`.

## Scala projects

### Step 1: Create a new sbt project called _docs_:

Edit `Build.scala` (or `build.sbt`) for adding:

```
lazy val docs = (project in file("docs"))
  .settings(moduleName := "your-library-docs")
  .settings(docsSettings: _*)
```

Where `docsSettings` could be the standard settings parameters as `organization`, `organizationName`, etc. Maybe it's interesting to set parameters in order to avoid publishing:

```
Seq(
    publish := (),
    publishLocal := (),
    publishArtifact := false)
```

### Step 2: Install `sbt-site`:

This great [plugin](https://github.com/sbt/sbt-site) generates project websites from static content, Jekyll, Sphinx, Pamflet, Nanoc, GitBook, and/or Asciidoctor, and can optionally include generated ScalaDoc.

- Add `addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.0.0")` in `plugins.sbt` file.
- Add `enablePlugins(JekyllPlugin)` in the `docsSettings` mentioned above.

Once done, you can compile the Jekyll microsite just with the task `makeSite` when the sbt project `docs` is active.

### Step 3: Install `sbt-ghpages`:

[This plugin](https://github.com/sbt/sbt-ghpages) moves the static site auto-generated by `sbt-site` into `gh-pages` branch, assuming that this branch already exists in your repo.

- Add these plugins in `plugins.sbt` file.

    ```
    addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.5.4" exclude("com.typesafe.sbt", "sbt-git"))
    addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.8.5")
    ```

- Add these config settings in the previously mentioned `docsSettings`:

    ```
    ghpages.settings ++
    Seq(git.remoteRepo := "git@github.com:47deg/<YOUR_REPO_NAME>.git")
    ```

Once added this plugin, the task `ghpages-push-site` will push the site.
