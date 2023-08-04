package com.akshar.store.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.akshar.store.base.viewmodel.MainViewModel;
import com.akshar.store.base.viewmodel.NavDrawerViewModel;
import com.akshar.store.repository.Repository;
import com.akshar.store.ui.addresssection.viewmodel.AddressViewModel;
import com.akshar.store.ui.cartsection.viewmodel.CartViewModel;
import com.akshar.store.ui.checkoutsection.viewmodel.CheckoutViewModel;
import com.akshar.store.ui.homesection.viewmodel.CategoriesViewModel;
import com.akshar.store.ui.homesection.viewmodel.HomeViewModel;
import com.akshar.store.ui.loginsection.viewmodel.LoginViewModel;
import com.akshar.store.ui.loginsection.viewmodel.RegisterViewModel;
import com.akshar.store.ui.notificationactivity.viewmodel.NotificationViewModel;
import com.akshar.store.ui.orderssection.viewmodel.OrderViewModel;
import com.akshar.store.ui.product_compare_section.viewmodel.CompareItemViewModel;
import com.akshar.store.ui.product_compare_section.viewmodel.CompareViewmodel;
import com.akshar.store.ui.product_review_section.viewmodel.AddProductReviewViewModel;
import com.akshar.store.ui.product_review_section.viewmodel.ReviewListingViewModel;
import com.akshar.store.ui.productsection.viewmodel.DownloadsViewModel;
import com.akshar.store.ui.productsection.viewmodel.ProductListViewModel;
import com.akshar.store.ui.productsection.viewmodel.ProductViewModel;
import com.akshar.store.ui.profilesection.viewmodel.ProfileViewModel;
import com.akshar.store.ui.schoolorder.viewmodel.SchoolViewModel;
import com.akshar.store.ui.searchsection.viewmodel.SearchViewModel;
import com.akshar.store.ui.sellersection.viewmodel.AddVendorReviewViewModel;
import com.akshar.store.ui.sellersection.viewmodel.SellerListViewModel;
import com.akshar.store.ui.sellersection.viewmodel.SellerShopViewModel;
import com.akshar.store.ui.sellersection.viewmodel.WriteVendorReviewViewModel;
import com.akshar.store.ui.wishlistsection.viewmodel.WishListViewModel;

import javax.inject.Inject;


public class ViewModelFactory implements ViewModelProvider.Factory {
    private Repository repository;

    @Inject
    public ViewModelFactory(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if(modelClass.isAssignableFrom(AddProductReviewViewModel.class))
        {
            return (T) new AddProductReviewViewModel(repository);
        }
        if(modelClass.isAssignableFrom(ReviewListingViewModel.class))
        {
            return (T) new ReviewListingViewModel(repository);
        }
        if(modelClass.isAssignableFrom(CompareViewmodel.class))
        {
            return (T) new CompareViewmodel(repository);
        }
 if(modelClass.isAssignableFrom(CompareItemViewModel.class))
        {
            return (T) new CompareItemViewModel(repository);
        }
if(modelClass.isAssignableFrom(NotificationViewModel.class))
        {
            return (T) new NotificationViewModel(repository);
        }

        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(repository);
        }
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(repository);
        }
        if (modelClass.isAssignableFrom(NavDrawerViewModel.class)) {
            return (T) new NavDrawerViewModel(repository);
        }
        if (modelClass.isAssignableFrom(ProductListViewModel.class)) {
            return (T) new ProductListViewModel(repository);
        }
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(repository);
        }
        if (modelClass.isAssignableFrom(RegisterViewModel.class)) {
            return (T) new RegisterViewModel(repository);
        }
        if (modelClass.isAssignableFrom(ProfileViewModel.class)) {
            return (T) new ProfileViewModel(repository);
        }
        if (modelClass.isAssignableFrom(AddressViewModel.class)) {
            return (T) new AddressViewModel(repository);
        }
        if (modelClass.isAssignableFrom(CategoriesViewModel.class)) {
            return (T) new CategoriesViewModel(repository);
        }
        if (modelClass.isAssignableFrom(WishListViewModel.class)) {
            return (T) new WishListViewModel(repository);
        }
        if (modelClass.isAssignableFrom(OrderViewModel.class)) {
            return (T) new OrderViewModel(repository);
        }
        if (modelClass.isAssignableFrom(SearchViewModel.class)) {
            return (T) new SearchViewModel(repository);
        }
        if (modelClass.isAssignableFrom(CartViewModel.class)) {
            return (T) new CartViewModel(repository);
        }
        if (modelClass.isAssignableFrom(ProductViewModel.class)) {
            return (T) new ProductViewModel(repository);
        }
        if (modelClass.isAssignableFrom(CheckoutViewModel.class)) {
            return (T) new CheckoutViewModel(repository);
        }
        if (modelClass.isAssignableFrom(CheckoutViewModel.class)) {
            return (T) new CheckoutViewModel(repository);
        }
        if (modelClass.isAssignableFrom(SellerListViewModel.class)) {
            return (T) new SellerListViewModel(repository);
        }
        if (modelClass.isAssignableFrom(SellerShopViewModel.class)) {
            return (T) new SellerShopViewModel(repository);
        }
        if (modelClass.isAssignableFrom(WriteVendorReviewViewModel.class)) {
            return (T) new WriteVendorReviewViewModel(repository);
        }
        if (modelClass.isAssignableFrom(AddVendorReviewViewModel.class)) {
            return (T) new AddVendorReviewViewModel(repository);
        }
        if (modelClass.isAssignableFrom(DownloadsViewModel.class)) {
            return (T) new DownloadsViewModel(repository);
        }
        if (modelClass.isAssignableFrom(SchoolViewModel.class)) {
            return (T) new SchoolViewModel(repository);
        }

        throw new IllegalArgumentException("Unknown class name");
    }
}
