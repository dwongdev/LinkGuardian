package dev.elbullazul.linkguardian.ui.fragments

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import dev.elbullazul.linkguardian.backends.Backend
import dev.elbullazul.linkguardian.backends.LinkwardenBackend
import dev.elbullazul.linkguardian.backends.features.PreviewProvider
import dev.elbullazul.linkguardian.data.extensions.Describable
import dev.elbullazul.linkguardian.data.extensions.Previewable
import dev.elbullazul.linkguardian.data.generic.Bookmark
import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenCollection
import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenLink
import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenTag
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookmarkFragment(
    bookmark: Bookmark,
    backend: Backend,
    showPreviews: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onTagClick: (String) -> Unit
) {
    val context = LocalContext.current

    Card(
        Modifier
            .fillMaxWidth()
            .padding(7.dp, 2.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(10.dp, 5.dp)
        ) {
            if (backend is PreviewProvider && bookmark is Previewable && showPreviews) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(backend.getPreviewUrl(bookmark))
                        .httpHeaders(backend.getHeaders())
                        .build(),
                    onError = { error ->
                        println(error)
                    },
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(80.dp)
                        .width(80.dp)
                        .padding(end = 8.dp)
                        .clip(shape = RoundedCornerShape(16.dp))
                )
            }
            Column {
                Text(linkedText(bookmark.shortName()))

                if (bookmark is Describable) {
                    Text(
                        text = bookmark.truncatedDescription(),
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }

                Row {
                    LazyHorizontalGrid(
                        rows = GridCells.Adaptive(minSize = 25.dp),
                        modifier = Modifier.height(25.dp).weight(1.0f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        items(bookmark.tags) { tag ->
                            TagFragment(tag, onTagClick)
                        }
                    }
                    // TODO: pin and/or favorite icons would go here
                }
            }
        }
    }
}

@Composable
fun linkedText(text: String): AnnotatedString {
    return buildAnnotatedString {
        append(text)
        addStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            ),
            start = 0,
            end = text.length
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LinkCardPreview() {
    LinkGuardianTheme(darkTheme = true) {
        BookmarkFragment(
            bookmark = LinkwardenLink(
                id = 0,
                name = "Demo",
                description = "Link description",
                url = "https://example.org",
                preview = "img/logo.png",
                tags = listOf(
                    LinkwardenTag(
                        id = 0,
                        name = "Test tag 1"
                    ),
                    LinkwardenTag(
                        id = 0,
                        name = "Test tag 2"
                    )
                ),
                collection = LinkwardenCollection(
                    id = 0,
                    name = "Nameless collection"
                )
            ),
            backend = LinkwardenBackend("","",""),
            showPreviews = true,
            onClick = {},
            onLongClick = {},
            onTagClick = {},
        )
    }
}