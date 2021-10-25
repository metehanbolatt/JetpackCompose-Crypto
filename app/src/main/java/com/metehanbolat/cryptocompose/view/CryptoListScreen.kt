package com.metehanbolat.cryptocompose.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.metehanbolat.cryptocompose.model.CryptoListItem
import com.metehanbolat.cryptocompose.viewmodel.CryptoListViewModel

@Composable
fun CryptoListScreen(
    navController: NavController,
    viewModel: CryptoListViewModel = hiltViewModel()
){

    Surface(
        color = MaterialTheme.colors.secondary,
        modifier = Modifier.fillMaxSize()
    ) {

        Column {
            Text(
                text = "Cryptos",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                textAlign = TextAlign.Center,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary
            )

            Spacer(modifier = Modifier.height(10.dp))
            SearchBar(
                hint =  "Search...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ){
                viewModel.searchCryptoList(it)
            }
            Spacer(modifier = Modifier.height(10.dp))
            CryptoList(navController = navController)
        }

    }
}

@Composable
fun SearchBar(
    modifier : Modifier = Modifier,
    hint : String = "",
    onSearch : (String) -> Unit = {}
){
    var text = remember { mutableStateOf("") }
    var isHintDisplayed = remember { mutableStateOf(hint != "") }
    
    Box(modifier = modifier){
        BasicTextField(
            value = text.value,
            onValueChange = {
                text.value = it
                onSearch(it) },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed.value = it.isFocused != true && text.value.isEmpty()
                }
        )

        if (isHintDisplayed.value){
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp))
        }
    }
}

@Composable
fun CryptoList(navController: NavController, viewModel: CryptoListViewModel = hiltViewModel()){
    val cryptoList = remember { viewModel.cryptoList }
    val errorMessage = remember { viewModel.errorMessage }
    val isLoading = remember { viewModel.isLoading }

    CryptoListView(cryptos = cryptoList.value, navController = navController)

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()){
        if (isLoading.value){
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if (errorMessage.value.isNotEmpty()){
            RetryView(error = errorMessage.value) {
                viewModel.loadCryptos()
            }
        }
    }
}

@Composable
fun CryptoListView(cryptos : List<CryptoListItem>, navController: NavController){
    LazyColumn(contentPadding = PaddingValues(5.dp)){
        items(cryptos){ crypto ->
            CryptoRow(navController = navController, crypto = crypto)
        }
    }
}

@Composable
fun CryptoRow(navController: NavController, crypto: CryptoListItem){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.secondary)
            .clickable {
                navController.navigate(
                    "crypto_detail_screen/${crypto.currency}/${crypto.price}"
                )
            }
    ) {
        Text(
            text = crypto.currency,
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(2.dp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary
        )

        Text(
            text = crypto.price,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(2.dp),
            color = MaterialTheme.colors.primaryVariant
        )
    }
}

@Composable
fun RetryView(error: String, onRetry : () -> Unit){
    Column {
        Text(text = error, color = Color.Red, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            onRetry
        }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Retry")
        }
    }
}
