package org.d3if3086.mobpro1.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3086.mobpro1.R
import org.d3if3086.mobpro1.database.BaksoDb
import org.d3if3086.mobpro1.ui.theme.MobPro1Theme
import org.d3if3086.mobpro1.util.ViewModelFactory

const val KEY_ID_BAKSO = "idBakso"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
    val db = BaksoDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = colorResource(R.color.brick)
                        )
                    }
                },
                title = {
                    if (id == null)
                        Text(text = stringResource(id = R.string.tambah_review))
                    else
                        Text(text = stringResource(id = R.string.edit_review))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = colorResource(R.color.arctic),
                    titleContentColor = colorResource(R.color.brick),
                ),
                actions = {
                    if (id != null) {
                        DeleteAction { showDialog = true }
                        DisplayAlertDialog(
                            openDialog = showDialog,
                            onDismissRequest = { showDialog = false }) {
                            showDialog = false
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        FormBakso(
            modifier = Modifier.padding(paddingValues),
            navController, id
        )
    }
}

@Composable
fun FormBakso(
    modifier: Modifier,
    navController: NavHostController,
    id: Long? = null
) {
    val context = LocalContext.current
    val db = BaksoDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var nama by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var review by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getBakso(id) ?: return@LaunchedEffect
        nama = data.nama
        deskripsi = data.deskripsi
        review = data.review
        rating = data.rating
    }

    val radioOptions = listOf(
        "Bintang 1",
        "Bintang 2",
        "Bintang 3",
        "Bintang 4",
        "Bintang 5",
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.intro)
        )
        OutlinedTextField(
            value = nama,
            onValueChange = { nama = it },
            label = { Text(text = stringResource(R.string.nama)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = deskripsi,
            onValueChange = { deskripsi = it },
            label = { Text(text = stringResource(R.string.isi_deskripsi)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = review,
            onValueChange = { review = it },
            label = { Text(text = stringResource(R.string.isi_review)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {
            radioOptions.forEach { text ->
                ClassOption(
                    label = text,
                    isSelected = rating == text,
                    onOptionSelected = { rating = it },
                    modifier = Modifier
                        .selectable(
                            selected = rating == text,
                            onClick = { rating = text },
                            role = Role.RadioButton
                        )
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    if (nama == "" || deskripsi == "" || review == "" || rating == "") {
                        Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if (id == null) {
                        viewModel.insert(nama, deskripsi, review, rating)
                    } else {
                        viewModel.update(id, nama, deskripsi, review, rating)
                    }
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(colorResource(R.color.arctic)),
            ) {
                Text(
                    text = stringResource(R.string.simpan),
                    color = colorResource(R.color.brick)
                )
            }
        }
    }
}

@Composable
fun ClassOption(
    label: String,
    isSelected: Boolean,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = { onOptionSelected(label) },
            colors = RadioButtonDefaults.colors(colorResource(R.color.brick))
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 1.dp)
        )
    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    IconButton(onClick = { delete() }) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.hapus),
            tint = colorResource(R.color.brick)
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview() {
    MobPro1Theme {
        DetailScreen(rememberNavController())
    }
}