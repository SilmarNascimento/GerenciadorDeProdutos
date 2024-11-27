import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductsCreateEditComponent } from './products-create-edit.component';

describe('ProductsCreateComponent', () => {
  let component: ProductsCreateEditComponent;
  let fixture: ComponentFixture<ProductsCreateEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductsCreateEditComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductsCreateEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
