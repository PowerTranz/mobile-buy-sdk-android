package com.shopify.sample.view.products;

import android.support.annotation.NonNull;

import com.shopify.sample.domain.interactor.CollectionProductNextPageInteractor;
import com.shopify.sample.domain.interactor.RealCollectionProductNextPageInteractor;
import com.shopify.sample.domain.model.Product;
import com.shopify.sample.view.BasePaginatedListViewModel;
import com.shopify.sample.view.base.ListItemViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableTransformer;

import static com.shopify.sample.util.Util.checkNotNull;

@SuppressWarnings("WeakerAccess")
public class ProductPaginatedListViewModel extends BasePaginatedListViewModel<Product> {
  private final static int PER_PAGE = 10;

  private final String collectionId;
  private final CollectionProductNextPageInteractor collectionProductNextPageInteractor = new RealCollectionProductNextPageInteractor();

  public ProductPaginatedListViewModel(@NonNull final String collectionId) {
    this.collectionId = checkNotNull(collectionId, "collectionId == null");
  }

  @Override protected ObservableTransformer<String, List<Product>> nextPageRequestComposer() {
    return upstream -> upstream.flatMapSingle(
      cursor -> collectionProductNextPageInteractor.execute(collectionId, cursor, PER_PAGE * 2)
    );
  }

  @NonNull @Override protected List<ListItemViewModel> convertAndMerge(@NonNull final List<Product> newItems,
    @NonNull final List<ListItemViewModel> existingItems) {
    List<ListItemViewModel> viewModels = new ArrayList<>();
    for (Product product : newItems) {
      viewModels.add(new ProductListItemViewModel(product));
    }
    List<ListItemViewModel> result = new ArrayList<>(existingItems);
    result.addAll(viewModels);
    return result;
  }
}
