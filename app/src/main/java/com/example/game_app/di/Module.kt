import android.webkit.WebViewClient
import com.example.game_app.viewModels.SplashViewModel
import com.example.game_app.viewModels.WebViewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.get
import org.koin.dsl.module

val mainModule = module {

    viewModel{
        WebViewViewModel()
    }

    viewModel {
        SplashViewModel()
    }

    single {
        WebViewClient()
    }
}