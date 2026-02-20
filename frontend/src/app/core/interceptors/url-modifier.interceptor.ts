import { HttpInterceptorFn } from '@angular/common/http';
import { environment } from 'src/environments/environment';

export const urlModifier: HttpInterceptorFn = (req, next) => {
  return next(req.clone({
    url: environment.backendUrl + req.url,
    withCredentials: true
  }));
};
